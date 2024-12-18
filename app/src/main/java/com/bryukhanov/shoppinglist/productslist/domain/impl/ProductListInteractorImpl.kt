package com.bryukhanov.shoppinglist.productslist.domain.impl

import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListInteractor
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListRepository
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.flow.Flow

class ProductListInteractorImpl(private val productListRepository: ProductListRepository) :
    ProductListInteractor {

    override fun getAllProducts(shoppingListId: Int): Flow<List<ProductListItem>> {
        return productListRepository.getAllProducts(shoppingListId)
    }

    override suspend fun deleteAllProducts(shoppingListId: Int) {
        productListRepository.deleteAllProducts(shoppingListId)
    }

    override suspend fun deleteBoughtProducts(shoppingListId: Int) {
        productListRepository.deleteBoughtProducts(shoppingListId)
    }

    override suspend fun deleteProduct(productListItem: ProductListItem) {
        productListRepository.deleteProduct(productListItem)
    }

    override suspend fun addProduct(productListItem: ProductListItem) {
        productListRepository.addProduct(productListItem)
    }

    override suspend fun updateProduct(productListItem: ProductListItem) {
        productListRepository.updateProduct(productListItem)
    }

    override suspend fun getProductById(productId: Int): ProductListItem? {
        return productListRepository.getProductById(productId)
    }

    override suspend fun updateSwapProducts(swapItems: List<ProductListItem>) {
        productListRepository.updateSwapProducts(swapItems)
    }
}