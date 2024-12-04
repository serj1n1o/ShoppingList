package com.bryukhanov.shoppinglist.productslist.domain.impl

import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListInteractor
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListRepository
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.flow.Flow

class ProductListInteractorImpl(private val productListRepository: ProductListRepository) :
    ProductListInteractor {
    override fun getAllProducts(shoppingListId: Int): Flow<List<ProductListItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllProducts(shoppingListId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBoughtProducts(shoppingListId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(productListItem: ProductListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun addProduct(productListItem: ProductListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(productListItem: ProductListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductById(productId: Int): ProductListItem {
        TODO("Not yet implemented")
    }
}