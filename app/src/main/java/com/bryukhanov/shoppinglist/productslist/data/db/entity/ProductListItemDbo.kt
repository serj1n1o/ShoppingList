package com.bryukhanov.shoppinglist.productslist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.bryukhanov.shoppinglist.mylists.data.db.entity.ShoppingListItemDbo

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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "shoppingListId")
    val shoppingListId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "amount")
    val amount: Int?,
    @ColumnInfo(name = "unit")
    val unit: String?,
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "isBought")
    val isBought: Boolean,
)
