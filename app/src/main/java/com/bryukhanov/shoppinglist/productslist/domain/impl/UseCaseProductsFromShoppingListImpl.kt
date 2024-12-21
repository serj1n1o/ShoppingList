package com.bryukhanov.shoppinglist.productslist.domain.impl

import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListRepository
import com.bryukhanov.shoppinglist.productslist.domain.api.UseCaseProductsFromShoppingList
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.flow.Flow

class UseCaseProductsFromShoppingListImpl(private val productListRepository: ProductListRepository) :
    UseCaseProductsFromShoppingList {

    override suspend fun getProductsFromShoppingList(shoppingListId: Int): Flow<Result<List<ProductListItem>>> {
        return productListRepository.getAllProducts(shoppingListId)
    }

    override suspend fun addProducts(products: List<ProductListItem>) {
        productListRepository.addProducts(products)
    }
}