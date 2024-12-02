package com.bryukhanov.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bryukhanov.shoppinglist.db.entity.ProductListItemDbo

@Dao
interface ProductListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(productListItemDbo: ProductListItemDbo)

    @Update
    suspend fun updateProduct(productListItemDbo: ProductListItemDbo)

    @Delete
    suspend fun deleteProduct(productListItemDbo: ProductListItemDbo)

    @Query("SELECT * FROM product_list WHERE shoppingListId = :shoppingListId ORDER BY position")
    suspend fun getAllProductsForShoppingList(shoppingListId: Int): List<ProductListItemDbo>

    @Query("SELECT * FROM product_list WHERE shoppingListId = :shoppingListId ORDER BY name")
    suspend fun getAllProductsForShoppingListByAlphabetSort(shoppingListId: Int): List<ProductListItemDbo>

    @Query("DELETE FROM product_list")
    suspend fun deleteAllProductLists()
}