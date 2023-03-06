package com.shddx.product

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document("product")
internal data class Product(
    @Id
    val id: String? = null,
    val name: String,
    val description: String,
    val price: BigDecimal
)
