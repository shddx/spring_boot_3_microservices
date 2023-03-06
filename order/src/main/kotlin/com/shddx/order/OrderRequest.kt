package com.shddx.order

import org.springframework.stereotype.Repository

@Repository
internal data class OrderRequest(val orderLineItems: List<OrderLineItemDto>) {

}
