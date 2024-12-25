package com.bryukhanov.shoppinglist.core.util

import android.content.Context
import com.bryukhanov.shoppinglist.R

enum class Units(private val resourceId: Int) {
    PIECE(R.string.unit_piece),
    PACKAGE(R.string.unit_package),
    KILOGRAM(R.string.unit_kilogram),
    GRAM(R.string.unit_gram),
    LITER(R.string.unit_liter),
    MILLILITER(R.string.unit_milliliter),
    PACK(R.string.unit_pack);

    fun getDisplayName(context: Context): String {
        return context.getString(this.resourceId)
    }
}