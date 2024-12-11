package com.bryukhanov.shoppinglist.di

import com.bryukhanov.shoppinglist.mylists.presentation.viewmodel.MyListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<MyListsViewModel> {
        MyListsViewModel(shoppingListInteractor = get())
    }
}