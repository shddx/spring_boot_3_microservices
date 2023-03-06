package com.shddx.product

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
internal class ProductService(val productRepo: ProductRepo) {
    private val logger = KotlinLogging.logger {}

    fun createProduct(request: ProductRequest) {
        val product = Product(
            name = request.name,
            description = request.description,
            price = request.price
        ).let {
            productRepo.save(it)
        }
        logger.info { "Product ${product.id} is saved" }
    }

    fun getProducts(): List<ProductResponse> {
        return productRepo.findAll().map {
            ProductResponse(
                id = it.id!!,
                name = it.name,
                description = it.description,
                price = it.price
            )
        }
    }
}