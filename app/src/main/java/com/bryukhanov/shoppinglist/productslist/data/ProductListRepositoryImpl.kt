package com.bryukhanov.shoppinglist.productslist.data

import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toDbo
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toDboProductList
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toUi
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toUiProductList
import com.bryukhanov.shoppinglist.productslist.data.db.dao.ProductListDao
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListRepository
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductListRepositoryImpl(
    private val productListDao: ProductListDao,
) : ProductListRepository {

    override fun getAllProducts(shoppingListId: Int): Flow<Result<List<ProductListItem>>> {
        return productListDao.getAllProductsFromShoppingList(shoppingListId)
            .map { listDbo ->
                try {
                    Result.success(listDbo.toUiProductList())
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteAllProducts(shoppingListId: Int): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                productListDao.deleteAllProductLists(shoppingListId)
            }
        }
    }

    override suspend fun deleteBoughtProducts(shoppingListId: Int): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                productListDao.deleteBoughtProducts(shoppingListId)
            }
        }
    }

    override suspend fun deleteProduct(productListItem: ProductListItem): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                productListDao.deleteProduct(productListItem.toDbo())
            }
        }
    }

    override suspend fun addProduct(productListItem: ProductListItem): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                productListDao.addProduct(productListItem.toDbo())
            }
        }
    }

    override suspend fun updateProduct(productListItem: ProductListItem): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                productListDao.updateProduct(productListItem.toDbo())
            }
        }
    }

    override suspend fun getProductById(productId: Int): ProductListItem? {
        return withContext(Dispatchers.IO) {
            productListDao.getProductById(productId)?.toUi()
        }
    }

    override suspend fun updateSwapProducts(swapItems: List<ProductListItem>): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                productListDao.updateSwapProducts(swapItems.toDboProductList())
            }
        }
    }

    override suspend fun addProducts(products: List<ProductListItem>): Result<Unit> {
        return kotlin.runCatching {
            withContext(Dispatchers.IO) {
                productListDao.addProducts(products.toDboProductList())
            }
        }
    }

}