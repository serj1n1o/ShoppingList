package com.bryukhanov.shoppinglist.mylists.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.launch

class MyListsViewModel(private val repository: ShoppingListRepository) : ViewModel() {

    private val _shoppingLists = MutableLiveData<List<ShoppingListItem>>(emptyList())
    val shoppingLists: LiveData<List<ShoppingListItem>> get() = _shoppingLists


    fun addShoppingList(item: ShoppingListItem) {
        val currentList = _shoppingLists.value ?: emptyList()
        _shoppingLists.value = currentList + item
    }

    fun updateShoppingList(item: ShoppingListItem) {
        viewModelScope.launch {
            repository.updateShoppingList(item)
        }
    }

    fun deleteShoppingList(item: ShoppingListItem) {
        viewModelScope.launch {
            repository.deleteShoppingList(item)
        }
    }

    fun deleteAllShoppingLists() {
        viewModelScope.launch {
            repository.deleteAllLists()
        }
    }

    fun copyShoppingList(shoppingListId: Int) {
        viewModelScope.launch {
            repository.copyShoppingList(shoppingListId)
        }
    }
}


