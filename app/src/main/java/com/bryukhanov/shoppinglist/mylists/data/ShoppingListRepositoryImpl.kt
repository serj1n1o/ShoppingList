package com.bryukhanov.shoppinglist.mylists.data

import com.bryukhanov.shoppinglist.db.DataBase
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.flow.Flow

class ShoppingListRepositoryImpl(
    private val dataBase: DataBase,
    shoppingListConverter: ShoppingListConverter,
) : ShoppingListRepository {
    override fun getAllShoppingLists(): Flow<List<ShoppingListItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun addShoppingList(shoppingListItem: ShoppingListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun updateShoppingList(shoppingListItem: ShoppingListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteShoppingList(shoppingListItem: ShoppingListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllLists() {
        TODO("Not yet implemented")
    }
}