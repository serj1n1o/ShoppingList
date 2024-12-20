package com.bryukhanov.shoppinglist.productslist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bryukhanov.shoppinglist.productslist.data.db.entity.ProductListItemDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(productListItemDbo: ProductListItemDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducts(productsListItemDbo: List<ProductListItemDbo>)

    @Update
    suspend fun updateProduct(productListItemDbo: ProductListItemDbo)

    @Delete
    suspend fun deleteProduct(productListItemDbo: ProductListItemDbo)

    @Query("SELECT * FROM product_list WHERE shoppingListId = :shoppingListId")
    fun getAllProductsFromShoppingList(shoppingListId: Int): Flow<List<ProductListItemDbo>>

    @Query("DELETE FROM product_list WHERE shoppingListId = :shoppingListId")
    suspend fun deleteAllProductLists(shoppingListId: Int)

    @Query("DELETE FROM product_list WHERE shoppingListId = :shoppingListId AND isBought = 1")
    suspend fun deleteBoughtProducts(shoppingListId: Int)

    @Query("SELECT * FROM product_list WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductListItemDbo?

    @Update
    suspend fun updateSwapProducts(products: List<ProductListItemDbo>)
}