package com.bryukhanov.shoppinglist.productslist.domain.models


data class ProductListItem(
    val id: Int = 0,
    val shoppingListId: Int,
    val name: String,
    val amount: Int?,
    val unit: String?,
    val position: Int,
    val isBought: Boolean = false,
)