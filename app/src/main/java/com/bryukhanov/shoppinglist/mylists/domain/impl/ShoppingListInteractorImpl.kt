package com.bryukhanov.shoppinglist.mylists.domain.impl

import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListInteractor
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.flow.Flow

class ShoppingListInteractorImpl(private val shoppingListRepository: ShoppingListRepository) :
    ShoppingListInteractor {

    override fun getAllShoppingLists(): Flow<List<ShoppingListItem>> {
        return shoppingListRepository.getAllShoppingLists()
    }

    override suspend fun addShoppingList(shoppingListItem: ShoppingListItem) {
        shoppingListRepository.addShoppingList(shoppingListItem)
    }

    override suspend fun updateShoppingList(shoppingListItem: ShoppingListItem) {
        shoppingListRepository.updateShoppingList(shoppingListItem)
    }

    override suspend fun deleteShoppingList(shoppingListItem: ShoppingListItem) {
        shoppingListRepository.deleteShoppingList(shoppingListItem)
    }

    override suspend fun deleteAllLists() {
        shoppingListRepository.deleteAllLists()
    }
}