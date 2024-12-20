package com.bryukhanov.shoppinglist.mylists.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListInteractor
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyListsViewModel(private val shoppingListInteractor: ShoppingListInteractor) : ViewModel() {

    private val listState = MutableLiveData<MyListsState>()
    fun getListState(): LiveData<MyListsState> = listState

    private val _searchResults = MutableLiveData<List<ShoppingListItem>>()
    val searchResults: LiveData<List<ShoppingListItem>> get() = _searchResults

    private val _isSearchEmpty = MutableLiveData<Boolean>()
    val isSearchEmpty: LiveData<Boolean> get() = _isSearchEmpty

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

    fun searchShoppingLists(query: String, originalList: List<ShoppingListItem>) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                withContext(Dispatchers.Main) {
                    _searchResults.postValue(emptyList())
                    _isSearchEmpty.postValue(true)
                }
            } else {
                val filteredList = originalList.filter {
                    it.name.startsWith(query, ignoreCase = true)
                }
                withContext(Dispatchers.Main) {
                    _searchResults.postValue(filteredList)
                    _isSearchEmpty.postValue(filteredList.isEmpty())
                }
            }
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

}


