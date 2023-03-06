package com.shddx.order

import io.micrometer.tracing.Tracer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
internal class OrderService(
    val orderRepo: OrderRepo,
    val orderLineItemRepo: OrderLineItemRepo,
    val inventoryClient: InventoryClient,
    val tracer: Tracer
) {

    @Transactional
    fun placeOrder(orderRequest: OrderRequest): String {
        val span = tracer.nextSpan().name("InventoryServiceLookup")
        span.start()
        try {
            tracer.withSpan(span).use {
                checkStock(orderRequest)
                saveOrder(orderRequest)
            }
            return "Order placed successfully"
        } finally {
            span.end()
        }
    }

    private fun saveOrder(orderRequest: OrderRequest) {
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

    private fun checkStock(orderRequest: OrderRequest) {
        val response = inventoryClient.checkInventory(
            InventoryRequest(
                skuCode = orderRequest.orderLineItems.map { it.skuCode }
            )
        )
        if (response.any { !it.isInStock }) {
            throw OutOfStockException(
                "One or more items are out of stock: ${
                    response.filter { !it.isInStock }.joinToString { it.skuCode }
                }"
            )
        }
    }

    data class InventoryRequest(val skuCode: List<String>)

    data class InventoryResponse(val skuCode: String, val isInStock: Boolean)
}