package com.bryukhanov.shoppinglist.mylists.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bryukhanov.shoppinglist.mylists.data.db.entity.ShoppingListItemDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun addShoppingList(shoppingListItemDbo: ShoppingListItemDbo): Long

    @Query("SELECT * FROM shopping_list")
    fun getAllShoppingList(): Flow<List<ShoppingListItemDbo>>

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    suspend fun getShoppingListById(id: Int): ShoppingListItemDbo?

    @Update
    suspend fun updateShoppingList(shoppingListItemDbo: ShoppingListItemDbo)

    @Delete
    suspend fun deleteShoppingListItem(shoppingListItemDbo: ShoppingListItemDbo)

    @Query("DELETE FROM shopping_list")
    suspend fun deleteAllShoppingList()
}