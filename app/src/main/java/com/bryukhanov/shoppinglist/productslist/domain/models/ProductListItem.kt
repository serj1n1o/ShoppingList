package com.bryukhanov.shoppinglist.productslist.domain.models

data class ProductListItem(
    var id: Int,
    val name: String,
    val amount: Int,
    val unit: String,
)