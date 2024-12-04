package com.bryukhanov.shoppinglist.di

import androidx.room.Room
import com.bryukhanov.shoppinglist.db.DataBase
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter
import org.koin.dsl.module

val dataModule = module {

    factory {
        ShoppingListConverter()
    }

    single {
        Room.databaseBuilder(
            context = get(),
            klass = DataBase::class.java,
            name = "shopping_list_db"
        ).build()
    }

}