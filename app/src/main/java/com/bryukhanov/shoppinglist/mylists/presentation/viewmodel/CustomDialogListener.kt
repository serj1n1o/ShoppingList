package com.bryukhanov.shoppinglist.mylists.presentation.viewmodel

import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem

interface CustomDialogListener {
    fun onPositiveClick(item: ShoppingListItem? = null)
    fun onNegativeClick()
}