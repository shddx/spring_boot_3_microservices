package com.shddx.order

import java.math.BigDecimal

internal data class OrderLineItemDto(val id: Long,
                                     val skuCode: String,
                                     val price: BigDecimal,
                                     val quantity: Int) {

}
