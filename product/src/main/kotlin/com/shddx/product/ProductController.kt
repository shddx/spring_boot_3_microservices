package com.shddx.product

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
internal class ProductController(val productService: ProductService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody request: ProductRequest) {
        productService.createProduct(request)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getProducts(): List<ProductResponse> {
        return productService.getProducts()
    }
}