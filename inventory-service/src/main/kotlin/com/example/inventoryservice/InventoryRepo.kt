package com.example.inventoryservice

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InventoryRepo: JpaRepository<Inventory, Long> {
    fun findBySkuCodeIn(skuCode: List<String>): List<Inventory>
}