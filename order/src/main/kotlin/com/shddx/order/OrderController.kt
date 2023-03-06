package com.shddx.order

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import io.github.resilience4j.timelimiter.annotation.TimeLimiter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/order")
internal class OrderController(val orderService: OrderService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "placeOrderFallback")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    fun placeOrder(@RequestBody orderRequest: OrderRequest): CompletableFuture<String> {
        return CompletableFuture.supplyAsync { orderService.placeOrder(orderRequest) }
    }

    fun placeOrderFallback(orderRequest: OrderRequest, ex: RuntimeException): CompletableFuture<String> {
        return CompletableFuture.supplyAsync { "Order failed due to ${ex.message}" }
    }
}