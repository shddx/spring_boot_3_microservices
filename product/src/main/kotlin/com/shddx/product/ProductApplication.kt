package com.shddx.product

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
internal class ProductApplication

fun main(args: Array<String>) {
    runApplication<ProductApplication>(*args)
}
