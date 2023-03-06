package com.example.inventoryservice

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InventoryService(val inventoryRepo: InventoryRepo) {

    fun isInStock(skuCode: List<String>): List<InventoryResponse> {
        return inventoryRepo.findBySkuCodeIn(skuCode)
            .map {
                InventoryResponse(it.skuCode, it.quantity > 0)
            }
    }
}