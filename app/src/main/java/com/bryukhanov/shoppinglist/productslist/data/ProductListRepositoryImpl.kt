package com.bryukhanov.shoppinglist.productslist.data

import com.bryukhanov.shoppinglist.db.DataBase
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toDbo
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toUi
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListRepository
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductListRepositoryImpl(
    private val dataBase: DataBase,
    private val shoppingListConverter: ShoppingListConverter,
) : ProductListRepository {

    override fun getAllProducts(shoppingListId: Int): Flow<List<ProductListItem>> {
        // доработать метод, в завиимости от выбранной сортировки просить нужный метод в DAO
        return dataBase.productListDao().getAllProductsForShoppingList(shoppingListId)
            .map { listDbo ->
                shoppingListConverter.productListDboToProductList(listDbo)
            }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteAllProducts(shoppingListId: Int) {
        withContext(Dispatchers.IO) {
            dataBase.productListDao().deleteAllProductLists(shoppingListId)
        }
    }

    override suspend fun deleteBoughtProducts(shoppingListId: Int) {
        withContext(Dispatchers.IO) {
            dataBase.productListDao().deleteBoughtProducts(shoppingListId)
        }
    }

    override suspend fun deleteProduct(productListItem: ProductListItem) {
        withContext(Dispatchers.IO) {
            dataBase.productListDao().deleteProduct(productListItem.toDbo())
        }
    }

    override suspend fun addProduct(productListItem: ProductListItem) {
        withContext(Dispatchers.IO) {
            dataBase.productListDao().addProduct(productListItem.toDbo())
        }
    }

    override suspend fun updateProduct(productListItem: ProductListItem) {
        withContext(Dispatchers.IO) {
            dataBase.productListDao().updateProduct(productListItem.toDbo())
        }
    }

    override suspend fun getProductById(productId: Int): ProductListItem? {
        return withContext(Dispatchers.IO) {
            dataBase.productListDao().getProductById(productId)?.toUi()
        }
    }
}