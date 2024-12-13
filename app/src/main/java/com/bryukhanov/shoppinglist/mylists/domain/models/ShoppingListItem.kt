package com.bryukhanov.shoppinglist.mylists.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingListItem(
    val id: Int = 0,
    val name: String,
    val cover: Int?,
) : Parcelable