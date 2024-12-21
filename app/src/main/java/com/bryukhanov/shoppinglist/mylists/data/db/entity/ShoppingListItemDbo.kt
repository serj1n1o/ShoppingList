package com.bryukhanov.shoppinglist.mylists.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListItemDbo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cover")
    val cover: Int?,
    @ColumnInfo(name = "sortType")
    var sortType: String,
)
