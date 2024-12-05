package com.bryukhanov.shoppinglist.mylists.domain.api

import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.flow.Flow

interface ShoppingListInteractor {

    fun getAllShoppingLists(): Flow<List<ShoppingListItem>>

    suspend fun addShoppingList(shoppingListItem: ShoppingListItem)

    suspend fun updateShoppingList(shoppingListItem: ShoppingListItem)

    suspend fun deleteShoppingList(shoppingListItem: ShoppingListItem)

    suspend fun deleteAllLists()
}