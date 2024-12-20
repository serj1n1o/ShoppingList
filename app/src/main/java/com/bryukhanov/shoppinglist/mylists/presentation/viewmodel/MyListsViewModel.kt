package com.bryukhanov.shoppinglist.mylists.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryukhanov.shoppinglist.core.util.SingleLiveEvent
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

    private val operationStatus = SingleLiveEvent<Result<Unit>>()
    fun getOperationStatus(): LiveData<Result<Unit>> = operationStatus

    fun getAllShoppingLists() {
        viewModelScope.launch {
            shoppingListInteractor.getAllShoppingLists().collect { result ->
                result.onSuccess { myList ->
                    processResult(myList)
                }.onFailure { processResult(emptyList()) }
            }
        }
    }

    fun addShoppingList(list: ShoppingListItem) {
        viewModelScope.launch {
            val result = shoppingListInteractor.addShoppingList(list)
            operationStatus.postValue(result)
        }
    }

    fun updateShoppingList(list: ShoppingListItem) {
        viewModelScope.launch {
            val result = shoppingListInteractor.updateShoppingList(list)
            operationStatus.postValue(result)
        }
    }

    fun deleteShoppingList(list: ShoppingListItem) {
        viewModelScope.launch {
            val result = shoppingListInteractor.deleteShoppingList(list)
            operationStatus.postValue(result)
        }
    }

    fun deleteAllShoppingLists() {
        viewModelScope.launch {
            val result = shoppingListInteractor.deleteAllLists()
            operationStatus.postValue(result)
        }
    }

    fun copyShoppingList(shoppingListId: Int) {
        viewModelScope.launch {
            val result = shoppingListInteractor.copyShoppingList(shoppingListId)
            operationStatus.postValue(result)
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