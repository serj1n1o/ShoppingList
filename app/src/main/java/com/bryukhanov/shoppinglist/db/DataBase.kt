package com.bryukhanov.shoppinglist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bryukhanov.shoppinglist.mylists.data.db.dao.ShoppingListDao
import com.bryukhanov.shoppinglist.mylists.data.db.entity.ShoppingListItemDbo
import com.bryukhanov.shoppinglist.productslist.data.db.dao.ProductListDao
import com.bryukhanov.shoppinglist.productslist.data.db.entity.ProductListItemDbo

@Database(entities = [ShoppingListItemDbo::class, ProductListItemDbo::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun productListDao(): ProductListDao
}