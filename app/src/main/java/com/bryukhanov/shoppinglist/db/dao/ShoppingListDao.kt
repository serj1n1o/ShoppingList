package com.bryukhanov.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bryukhanov.shoppinglist.db.entity.ShoppingListItemDbo

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShoppingList(shoppingListItemDbo: ShoppingListItemDbo)

    @Query("SELECT * FROM shopping_list")
    suspend fun getAllShoppingList(): List<ShoppingListItemDbo>

    @Update
    suspend fun updateShoppingList(shoppingListItemDbo: ShoppingListItemDbo)

    @Delete
    suspend fun deleteShoppingListItem(shoppingListItemDbo: ShoppingListItemDbo)

    @Query("DELETE FROM shopping_list")
    suspend fun deleteAllShoppingList()
}