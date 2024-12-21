package com.bryukhanov.shoppinglist.productslist.presentation.viewmodel

import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem

sealed interface ProductsState {

    data object Empty : ProductsState

    data class Content(val productList: List<ProductListItem>) : ProductsState
}