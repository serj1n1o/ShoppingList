package com.bryukhanov.shoppinglist.mylists.domain.impl

import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListInteractor
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.flow.Flow

class ShoppingListInteractorImpl(private val shoppingListRepository: ShoppingListRepository) :
    ShoppingListInteractor {
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