package com.bryukhanov.shoppinglist.mylists.domain.impl

import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListInteractor
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.flow.Flow

class ShoppingListInteractorImpl(private val shoppingListRepository: ShoppingListRepository) :
    ShoppingListInteractor {

    override fun getAllShoppingLists(): Flow<Result<List<ShoppingListItem>>> {
        return shoppingListRepository.getAllShoppingLists()
    }

    override suspend fun addShoppingList(shoppingListItem: ShoppingListItem): Result<Unit> {
        return shoppingListRepository.addShoppingList(shoppingListItem)
    }

    override suspend fun updateShoppingList(shoppingListItem: ShoppingListItem): Result<Unit> {
        return shoppingListRepository.updateShoppingList(shoppingListItem)
    }

    override suspend fun deleteShoppingList(shoppingListItem: ShoppingListItem): Result<Unit> {
        return shoppingListRepository.deleteShoppingList(shoppingListItem)
    }

    override suspend fun deleteAllLists(): Result<Unit> {
        return shoppingListRepository.deleteAllLists()
    }

    override suspend fun copyShoppingList(shoppingListId: Int): Result<Unit> {
        return shoppingListRepository.copyShoppingList(shoppingListId)
    }
}