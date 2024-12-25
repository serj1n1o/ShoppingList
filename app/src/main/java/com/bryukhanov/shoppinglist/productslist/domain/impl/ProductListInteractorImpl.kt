package com.bryukhanov.shoppinglist.productslist.domain.impl

import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListInteractor
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListRepository
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.flow.Flow

class ProductListInteractorImpl(private val productListRepository: ProductListRepository) :
    ProductListInteractor {

    override fun getAllProducts(shoppingListId: Int): Flow<Result<List<ProductListItem>>> {
        return productListRepository.getAllProducts(shoppingListId)
    }

    override suspend fun deleteAllProducts(shoppingListId: Int): Result<Unit> {
        return productListRepository.deleteAllProducts(shoppingListId)
    }

    override suspend fun deleteBoughtProducts(shoppingListId: Int): Result<Unit> {
        return productListRepository.deleteBoughtProducts(shoppingListId)
    }

    override suspend fun deleteProduct(productListItem: ProductListItem): Result<Unit> {
        return productListRepository.deleteProduct(productListItem)
    }

    override suspend fun addProduct(productListItem: ProductListItem): Result<Unit> {
        return productListRepository.addProduct(productListItem)
    }

    override suspend fun updateProduct(productListItem: ProductListItem): Result<Unit> {
        return productListRepository.updateProduct(productListItem)
    }

    override suspend fun getProductById(productId: Int): ProductListItem? {
        return productListRepository.getProductById(productId)
    }

    override suspend fun updateSwapProducts(swapItems: List<ProductListItem>): Result<Unit> {
        return productListRepository.updateSwapProducts(swapItems)
    }
}