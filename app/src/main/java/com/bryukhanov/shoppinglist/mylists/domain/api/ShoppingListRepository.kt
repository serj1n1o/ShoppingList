package com.bryukhanov.shoppinglist.mylists.domain.api

import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

    fun getAllShoppingLists(): Flow<Result<List<ShoppingListItem>>>

    suspend fun addShoppingList(shoppingListItem: ShoppingListItem): Result<Unit>

    suspend fun updateShoppingList(shoppingListItem: ShoppingListItem): Result<Unit>

    suspend fun deleteShoppingList(shoppingListItem: ShoppingListItem): Result<Unit>

    suspend fun deleteAllLists(): Result<Unit>

    suspend fun copyShoppingList(shoppingListId: Int): Result<Unit>

}