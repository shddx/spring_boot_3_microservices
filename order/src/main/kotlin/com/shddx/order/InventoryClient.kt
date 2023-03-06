package com.shddx.order

import org.springframework.http.MediaType
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange(
    accept = [MediaType.APPLICATION_JSON_VALUE]
)
internal interface InventoryClient {
    @PostExchange
    fun checkInventory(inventoryRequest: OrderService.InventoryRequest): Array<OrderService.InventoryResponse>
}