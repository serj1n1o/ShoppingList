package com.bryukhanov.shoppinglist.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_list",
    foreignKeys = [ForeignKey(
        entity = ShoppingListItemDbo::class,
        parentColumns = ["id"],
        childColumns = ["shoppingListId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductListItemDbo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shoppingListId: Int,
    val name: String,
    val amount: Int,
    val unit: String,
    val position: Int,
)
