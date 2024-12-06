package com.bryukhanov.shoppinglist.mylists.domain.models

data class ShoppingListItem(
    val id: Int = 0,
    val name: String,
    val cover: Int?,
)