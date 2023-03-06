package com.shddx.product

import org.springframework.data.mongodb.repository.MongoRepository

internal interface ProductRepo : MongoRepository<Product, String> {
}
