package com.bryukhanov.shoppinglist.mylists.data

import com.bryukhanov.shoppinglist.db.DataBase
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toDbo
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ShoppingListRepositoryImpl(
    private val dataBase: DataBase,
    private val shoppingListConverter: ShoppingListConverter,
) : ShoppingListRepository {
    override fun getAllShoppingLists(): Flow<List<ShoppingListItem>> = flow {
        val shoppingLists = withContext(Dispatchers.IO) {
            dataBase.shoppingListDao().getAllShoppingList()
        }
        emit(shoppingListConverter.shoppingListDboToShoppingList(shoppingLists))
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
}