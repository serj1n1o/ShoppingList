package com.bryukhanov.shoppinglist.productslist.domain.api

import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.flow.Flow

interface ProductListRepository {

    fun getAllProducts(shoppingListId: Int): Flow<List<ProductListItem>>

    suspend fun deleteAllProducts(shoppingListId: Int)

    suspend fun deleteBoughtProducts(shoppingListId: Int)

    suspend fun deleteProduct(productListItem: ProductListItem)

    suspend fun addProduct(productListItem: ProductListItem)

    suspend fun updateProduct(productListItem: ProductListItem)

    suspend fun getProductById(productId: Int): ProductListItem?

    suspend fun updateSwapProducts(swapItems: List<ProductListItem>)
}