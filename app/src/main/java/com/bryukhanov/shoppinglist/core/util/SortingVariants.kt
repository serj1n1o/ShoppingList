package com.bryukhanov.shoppinglist.core.util

import android.content.Context
import com.bryukhanov.shoppinglist.R

enum class SortingVariants(private val resourceId: Int) {
    ALPHABET(R.string.sort_alphabet_text),
    USER(R.string.sort_user_text);

    fun getDisplayName(context: Context): String {
        return context.getString(this.resourceId)
    }
}