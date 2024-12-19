package com.bryukhanov.shoppinglist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bryukhanov.shoppinglist.db.dao.ProductListDao
import com.bryukhanov.shoppinglist.db.dao.ShoppingListDao
import com.bryukhanov.shoppinglist.db.entity.ProductListItemDbo
import com.bryukhanov.shoppinglist.db.entity.ShoppingListItemDbo

@Database(entities = [ShoppingListItemDbo::class, ProductListItemDbo::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun productListDao(): ProductListDao
}