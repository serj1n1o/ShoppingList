package com.bryukhanov.shoppinglist.productslist.domain.api

import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.flow.Flow

interface ProductListRepository {

    fun getAllProducts(shoppingListId: Int): Flow<Result<List<ProductListItem>>>

    suspend fun deleteAllProducts(shoppingListId: Int): Result<Unit>

    suspend fun deleteBoughtProducts(shoppingListId: Int): Result<Unit>

    suspend fun deleteProduct(productListItem: ProductListItem): Result<Unit>

    suspend fun addProduct(productListItem: ProductListItem): Result<Unit>

    suspend fun updateProduct(productListItem: ProductListItem): Result<Unit>

    suspend fun getProductById(productId: Int): ProductListItem?

    suspend fun updateSwapProducts(swapItems: List<ProductListItem>): Result<Unit>
}