package com.bryukhanov.shoppinglist.mylists.presentation.viewmodel

import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem

sealed interface MyListsState {
    data object Empty : MyListsState

    data class Content(val myList: List<ShoppingListItem>) : MyListsState
}