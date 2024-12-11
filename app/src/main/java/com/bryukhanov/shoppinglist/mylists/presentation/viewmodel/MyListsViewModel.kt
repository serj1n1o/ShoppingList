package com.bryukhanov.shoppinglist.mylists.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListInteractor
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.launch

class MyListsViewModel(private val shoppingListInteractor: ShoppingListInteractor) : ViewModel() {

    private val listState = MutableLiveData<MyListsState>()
    fun getListState(): LiveData<MyListsState> = listState

    fun getAllShoppingLists() {
        viewModelScope.launch {
            shoppingListInteractor.getAllShoppingLists().collect { myList ->
                processResult(myList)
            }
        }
    }

    fun addShoppingList(list: ShoppingListItem) {
        viewModelScope.launch {
            shoppingListInteractor.addShoppingList(list)
        }
    }

    fun updateShoppingList(list: ShoppingListItem) {
        viewModelScope.launch {
            shoppingListInteractor.updateShoppingList(list)
        }
    }

    fun deleteShoppingList(list: ShoppingListItem) {
        viewModelScope.launch {
            shoppingListInteractor.deleteShoppingList(list)
        }
    }

    private fun processResult(shoppingList: List<ShoppingListItem>) {
        if (shoppingList.isEmpty()) {
            renderState(MyListsState.Empty)
        } else {
            renderState(MyListsState.Content(shoppingList))
        }
    }

    private fun renderState(state: MyListsState) {
        listState.postValue(state)
    }
    fun deleteAllShoppingLists() {
        viewModelScope.launch {
            shoppingListInteractor.deleteAllLists()
        }
    }

    fun copyShoppingList(shoppingListId: Int) {
        viewModelScope.launch {
            shoppingListInteractor.copyShoppingList(shoppingListId)
        }
    }
}


