package com.example.inventoryservice

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inventory")
class InventoryController(val inventoryService: InventoryService) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun isInStock(@RequestBody request: InventoryRequest): List<InventoryResponse> {
        return inventoryService.isInStock(request.skuCode)
    }
}