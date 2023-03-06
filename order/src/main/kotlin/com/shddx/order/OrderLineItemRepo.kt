package com.shddx.order

import org.springframework.data.jpa.repository.JpaRepository

internal interface OrderLineItemRepo: JpaRepository<OrderLineItem, Long> {
}