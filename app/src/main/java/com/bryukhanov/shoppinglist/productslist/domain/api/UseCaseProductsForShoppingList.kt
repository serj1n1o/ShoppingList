package com.bryukhanov.shoppinglist.productslist.domain.api

import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.flow.Flow

interface UseCaseProductsFromShoppingList {

    suspend fun getProductsFromShoppingList(shoppingListId: Int): Flow<Result<List<ProductListItem>>>

    suspend fun addProducts(products: List<ProductListItem>)
}