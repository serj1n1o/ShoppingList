package com.bryukhanov.shoppinglist.di

import androidx.room.Room
import com.bryukhanov.shoppinglist.core.util.NameShoppingListGenerator
import com.bryukhanov.shoppinglist.db.DataBase
import com.bryukhanov.shoppinglist.mylists.data.ShoppingListRepositoryImpl
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListInteractor
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.impl.ShoppingListInteractorImpl
import com.bryukhanov.shoppinglist.productslist.data.ProductListRepositoryImpl
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListInteractor
import com.bryukhanov.shoppinglist.productslist.domain.api.ProductListRepository
import com.bryukhanov.shoppinglist.productslist.domain.impl.ProductListInteractorImpl
import org.koin.dsl.module

val dataModule = module {

    single {
        NameShoppingListGenerator
    }

    single {
        Room.databaseBuilder(
            context = get(),
            klass = DataBase::class.java,
            name = "shopping_list_db"
        ).build()
    }

    factory<ShoppingListRepository> {
        ShoppingListRepositoryImpl(
            dataBase = get(),
            nameShoppingListGenerator = get()
        )
    }

    factory<ShoppingListInteractor> {
        ShoppingListInteractorImpl(shoppingListRepository = get())
    }

    factory<ProductListRepository> {
        ProductListRepositoryImpl(dataBase = get())
    }

    factory<ProductListInteractor> {
        ProductListInteractorImpl(productListRepository = get())
    }

}