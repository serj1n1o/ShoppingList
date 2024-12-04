package com.bryukhanov.shoppinglist.db.converters

import com.bryukhanov.shoppinglist.db.entity.ProductListItemDbo
import com.bryukhanov.shoppinglist.db.entity.ShoppingListItemDbo
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem

object ShoppingListConverter {

    fun ShoppingListItem.toDbo(): ShoppingListItemDbo {
        return ShoppingListItemDbo(
            id = id,
            name = name,
            cover = cover,
        )
    }

    fun ShoppingListItemDbo.toUi(): ShoppingListItem {
        return ShoppingListItem(
            id = id,
            name = name,
            cover = cover,
        )
    }

    fun ProductListItemDbo.toUi(): ProductListItem {
        return ProductListItem(
            id = id,
            shoppingListId = shoppingListId,
            name = name,
            amount = amount,
            unit = unit,
            position = position
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

    fun shoppingListDboToShoppingList(list: List<ShoppingListItemDbo>): List<ShoppingListItem> {
        return list.map { listDbo ->
            listDbo.toUi()
        }
    }

    fun productListDboToProductList(list: List<ProductListItemDbo>): List<ProductListItem> {
        return list.map { listDbo ->
            listDbo.toUi()
        }
    }
}