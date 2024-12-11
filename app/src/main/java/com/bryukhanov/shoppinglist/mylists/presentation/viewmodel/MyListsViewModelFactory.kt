package com.bryukhanov.shoppinglist.mylists.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository

class MyListsViewModelFactory(
    private val repository: ShoppingListRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyListsViewModel::class.java)) {
            return MyListsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
