package com.bryukhanov.shoppinglist.db.converters

import com.bryukhanov.shoppinglist.mylists.data.db.entity.ShoppingListItemDbo
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.productslist.data.db.entity.ProductListItemDbo
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem

object ShoppingListConverter {

    fun ShoppingListItem.toDbo(): ShoppingListItemDbo {
        return ShoppingListItemDbo(
            id = id,
            name = name,
            cover = cover,
            sortType = sortType,
        )
    }

    fun ShoppingListItemDbo.toUi(): ShoppingListItem {
        return ShoppingListItem(
            id = id,
            name = name,
            cover = cover,
            sortType = sortType,
        )
    }

    fun ProductListItemDbo.toUi(): ProductListItem {
        return ProductListItem(
            id = id,
            shoppingListId = shoppingListId,
            name = name,
            amount = amount,
            unit = unit,
            position = position,
            isBought = isBought
        )
    }

    fun ProductListItem.toDbo(): ProductListItemDbo {
        return ProductListItemDbo(
            id = id,
            shoppingListId = shoppingListId,
            name = name,
            amount = amount,
            unit = unit,
            position = position,
            isBought = isBought,
        )
    }

    fun List<ProductListItemDbo>.toUiProductList(): List<ProductListItem> {
        return this.map { it.toUi() }
    }

    fun List<ShoppingListItemDbo>.toUiShoppingList(): List<ShoppingListItem> {
        return this.map { it.toUi() }
    }

    fun List<ProductListItem>.toDboProductList(): List<ProductListItemDbo> {
        return this.map { it.toDbo() }
    }
}