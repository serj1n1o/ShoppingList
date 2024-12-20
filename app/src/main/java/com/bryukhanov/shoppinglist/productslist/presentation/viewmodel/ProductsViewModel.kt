package com.bryukhanov.shoppinglist.productslist.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryukhanov.shoppinglist.core.util.SingleLiveEvent
import com.bryukhanov.shoppinglist.core.util.SortingVariants
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListInteractor
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListInteractor
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val productListInteractor: ProductListInteractor,
    private val shoppingListInteractor: ShoppingListInteractor,
    private val application: Application,
) : ViewModel() {

    private val selectedSorting = MutableLiveData(SortingVariants.USER)
    lateinit var shoppingList: ShoppingListItem

    private val operationStatus = SingleLiveEvent<Result<Unit>>()
    fun getOperationStatus(): LiveData<Result<Unit>> = operationStatus

    fun getSelectedSorting(): LiveData<SortingVariants> = selectedSorting

    private val productState = MutableLiveData<ProductsState>()

    fun getProductState(): LiveData<ProductsState> = productState

    fun getProducts(shoppingListId: Int) {
        viewModelScope.launch {
            productListInteractor.getAllProducts(shoppingListId).collect { result ->
                result.onSuccess { processResult(it) }.onFailure { processResult(emptyList()) }
            }
        }
    }

    fun setSorting(sort: SortingVariants) {
        selectedSorting.value = sort
        updateSortTypeShoppingList(shoppingList)
    }

    private fun updateSortTypeShoppingList(shoppingList: ShoppingListItem) {
        selectedSorting.value?.let {
            if (shoppingList.sortType != selectedSorting.value!!.getDisplayName(application.applicationContext)) {
                viewModelScope.launch {
                    shoppingListInteractor.updateShoppingList(
                        shoppingList.copy(
                            sortType = selectedSorting.value!!.getDisplayName(
                                application.applicationContext
                            )
                        )
                    )
                }
            }
        }
    }

    fun sortProducts(sortType: SortingVariants, list: List<ProductListItem>? = null) {
        var currentList: List<ProductListItem>? = null
        var sortedList: List<ProductListItem>? = null
        currentList = list ?: (productState.value as? ProductsState.Content)?.productList
        sortedList = when (sortType) {
            SortingVariants.ALPHABET -> currentList?.sortedBy { it.name }
            SortingVariants.USER -> currentList?.sortedByDescending { it.position }
        }
        productState.value = sortedList?.let { ProductsState.Content(it) }
    }

    fun addProduct(product: ProductListItem) {
        viewModelScope.launch {
            val result = productListInteractor.addProduct(product)
            operationStatus.postValue(result)
        }

    }

    fun updateProduct(product: ProductListItem) {
        viewModelScope.launch {
            val result = productListInteractor.updateProduct(product)
            operationStatus.postValue(result)
        }
    }

    fun deleteProduct(product: ProductListItem) {
        viewModelScope.launch {
            val result = productListInteractor.deleteProduct(product)
            operationStatus.postValue(result)
        }
    }

    fun deleteAllProduct(shoppingListId: Int) {
        viewModelScope.launch {
            val result = productListInteractor.deleteAllProducts(shoppingListId)
            operationStatus.postValue(result)
        }
    }

    fun deleteBoughtProduct(shoppingListId: Int) {
        viewModelScope.launch {
            val result = productListInteractor.deleteBoughtProducts(shoppingListId)
            operationStatus.postValue(result)
        }
    }

    fun updateProductBoughtStatus(productId: Int, isBought: Boolean) {
        viewModelScope.launch {
            val product = productListInteractor.getProductById(productId)
            if (product != null) {
                updateProduct(product.copy(isBought = isBought))
            } else {
                operationStatus.postValue(Result.failure(Throwable()))
            }
        }
    }

    private fun processResult(productList: List<ProductListItem>) {
        if (productList.isEmpty()) {
            renderState(ProductsState.Empty)
        } else {
            selectedSorting.value?.let { sortProducts(it, productList) }
        }
    }

    private fun renderState(state: ProductsState) {
        productState.postValue(state)
    }

    fun updatePositionProducts(listSwapProducts: List<ProductListItem>) {
        viewModelScope.launch {
            val result = productListInteractor.updateSwapProducts(listSwapProducts)
            operationStatus.postValue(result)
        }
    }


}