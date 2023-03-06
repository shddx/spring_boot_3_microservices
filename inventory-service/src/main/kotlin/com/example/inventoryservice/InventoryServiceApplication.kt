package com.example.inventoryservice

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class InventoryServiceApplication {
	@Bean
	fun init(inventoryRepo: InventoryRepo) = CommandLineRunner {
		inventoryRepo.save(Inventory(1, "MacBook Pro M2", 100))
		inventoryRepo.save(Inventory(2, "MacBook Pro M2 Black", 0))
	}
}

fun main(args: Array<String>) {
	runApplication<InventoryServiceApplication>(*args)
}