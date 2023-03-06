package com.shddx.order

import io.micrometer.tracing.Tracer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Service
@Transactional(readOnly = true)
internal class OrderService(
    val orderRepo: OrderRepo,
    val orderLineItemRepo: OrderLineItemRepo,
    val webClientBuilder: WebClient.Builder,
    val tracer: Tracer
) {

    @Transactional
    fun placeOrder(orderRequest: OrderRequest): String {
        val span = tracer.nextSpan().name("InventoryServiceLookup")
        span.start()
        try {
            tracer.withSpan(span).use {
                val response = webClientBuilder.build().post()
                    .uri("http://inventory-service/api/inventory")
                    .bodyValue(
                        InventoryRequest(
                            skuCode = orderRequest.orderLineItems.map { it.skuCode }
                        )
                    )
                    .retrieve()
                    .bodyToMono(Array<InventoryResponse>::class.java)
                    .block() ?: throw IllegalArgumentException("Inventory service is down, please try again later")
                if (response.any { !it.isInStock }) {
                    throw OutOfStockException(
                        "One or more items are out of stock: ${
                            response.filter { !it.isInStock }.joinToString { it.skuCode }
                        }"
                    )
                }
                val order = Order(
                    orderNumber = UUID.randomUUID().toString()
                ).let {
                    orderRepo.save(it)
                }
                orderRequest.orderLineItems.map {
                    OrderLineItem(
                        order = order,
                        skuCode = it.skuCode,
                        quantity = it.quantity,
                        price = it.price
                    )
                }.let {
                    orderLineItemRepo.saveAll(it)
                }
            }
            return "Order placed successfully"
        } finally {
            span.end()
        }
    }

    data class InventoryRequest(val skuCode: List<String>)

    data class InventoryResponse(val skuCode: String, val isInStock: Boolean)
}