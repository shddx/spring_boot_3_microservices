package com.example.inventoryservice

data class InventoryResponse(
    val skuCode: String,
    val isInStock: Boolean
)
