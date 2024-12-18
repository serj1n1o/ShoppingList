package com.bryukhanov.shoppinglist.mylists.data

import com.bryukhanov.shoppinglist.core.util.NameShoppingListGenerator
import com.bryukhanov.shoppinglist.db.DataBase
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toDbo
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toUiShoppingList
import com.bryukhanov.shoppinglist.db.entity.ShoppingListItemDbo
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ShoppingListRepositoryImpl(
    private val dataBase: DataBase,
    private val nameShoppingListGenerator: NameShoppingListGenerator,
) : ShoppingListRepository {

    override fun getAllShoppingLists(): Flow<List<ShoppingListItem>> {
        return dataBase.shoppingListDao().getAllShoppingList().map { listDbo ->
            listDbo.toUiShoppingList()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addShoppingList(shoppingListItem: ShoppingListItem) {
        withContext(Dispatchers.IO) {
            dataBase.shoppingListDao().addShoppingList(shoppingListItem.toDbo())
        }
    }

    override suspend fun updateShoppingList(shoppingListItem: ShoppingListItem) {
        withContext(Dispatchers.IO) {
            dataBase.shoppingListDao().updateShoppingList(shoppingListItem.toDbo())
        }
    }

    override suspend fun deleteShoppingList(shoppingListItem: ShoppingListItem) {
        withContext(Dispatchers.IO) {
            dataBase.shoppingListDao().deleteShoppingListItem(shoppingListItem.toDbo())
        }
    }

    override suspend fun deleteAllLists() {
        withContext(Dispatchers.IO) {
            dataBase.shoppingListDao().deleteAllShoppingList()
        }
    }

    override suspend fun copyShoppingList(shoppingListId: Int) {
        withContext(Dispatchers.IO) {
            val originalList = dataBase.shoppingListDao().getShoppingListById(shoppingListId)
            val products =
                dataBase.productListDao().getAllProductsForShoppingList(shoppingListId).first()
            val existingNames = dataBase.shoppingListDao().getAllShoppingList().map { listDbo ->
                listDbo.map { it.name }
            }.first()
            if (originalList != null) {
                val newName = nameShoppingListGenerator.generateUniqueName(
                    baseName = originalList.name,
                    existingNames = existingNames
                )

                val newShoppingList =
                    ShoppingListItemDbo(
                        name = newName,
                        cover = originalList.cover,
                        sortType = originalList.sortType
                    )
                val newShoppingListId =
                    dataBase.shoppingListDao().addShoppingList(newShoppingList).toInt()

                val newProducts =
                    products.map { it.copy(id = 0, shoppingListId = newShoppingListId) }
                dataBase.productListDao().addProducts(newProducts)
            }
        }
    }

}