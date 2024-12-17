package com.bryukhanov.shoppinglist.productslist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bryukhanov.shoppinglist.core.util.SortingVariants
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListInteractor
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import kotlinx.coroutines.launch

class ProductsViewModel(private val productListInteractor: ProductListInteractor) : ViewModel() {

    private val selectedSorting = MutableLiveData(SortingVariants.USER)

    fun setSorting(sort: SortingVariants) {
        selectedSorting.postValue(sort)
    }

    fun getSelectedSorting(): LiveData<SortingVariants> = selectedSorting

    private val productState = MutableLiveData<ProductsState>()

    fun getProductState(): LiveData<ProductsState> = productState

    fun getProducts(shoppingListId: Int) {
        viewModelScope.launch {
            productListInteractor.getAllProducts(shoppingListId).collect { productList ->
                processResult(productList)
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
            productListInteractor.addProduct(product)
        }
    }

    fun updateProduct(product: ProductListItem) {
        viewModelScope.launch {
            productListInteractor.updateProduct(product)
        }
    }

    fun deleteProduct(product: ProductListItem) {
        viewModelScope.launch {
            productListInteractor.deleteProduct(product)
        }
    }

    fun deleteAllProduct(shoppingListId: Int) {
        viewModelScope.launch {
            productListInteractor.deleteAllProducts(shoppingListId)
        }
    }

    fun deleteBoughtProduct(shoppingListId: Int) {
        viewModelScope.launch {
            productListInteractor.deleteBoughtProducts(shoppingListId)
        }
    }

    fun updateProductBoughtStatus(productId: Int, isBought: Boolean) {
        viewModelScope.launch {
            val product = productListInteractor.getProductById(productId)
            if (product != null) {
                updateProduct(product.copy(isBought = isBought))
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
            productListInteractor.updateSwapProducts(listSwapProducts)
        }
    }


}