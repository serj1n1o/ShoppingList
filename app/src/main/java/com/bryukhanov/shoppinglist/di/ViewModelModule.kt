package com.bryukhanov.shoppinglist.di

import com.bryukhanov.shoppinglist.mylists.presentation.viewmodel.MyListsViewModel
import com.bryukhanov.shoppinglist.productslist.presentation.viewmodel.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<ProductsViewModel> {
        ProductsViewModel(
            productListInteractor = get(),
            shoppingListInteractor = get(),
            application = get()
        )
    }

    viewModel<MyListsViewModel> {
        MyListsViewModel(shoppingListInteractor = get())
    }
}