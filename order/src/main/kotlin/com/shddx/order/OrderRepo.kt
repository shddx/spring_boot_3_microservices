package com.shddx.order

import org.springframework.data.jpa.repository.JpaRepository

internal interface OrderRepo: JpaRepository<Order, Long> {
}