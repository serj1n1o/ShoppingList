package com.bryukhanov.shoppinglist.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListItemDbo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val cover: Int,
)
