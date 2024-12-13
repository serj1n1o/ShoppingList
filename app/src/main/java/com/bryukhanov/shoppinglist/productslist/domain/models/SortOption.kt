package com.bryukhanov.shoppinglist.productslist.domain.models

data class SortOption(
    val icon: Int,
    val text: String,
    var isSelected: Boolean = false,
)
