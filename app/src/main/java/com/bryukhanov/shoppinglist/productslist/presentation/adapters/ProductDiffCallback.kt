package com.bryukhanov.shoppinglist.productslist.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem

class ProductDiffCallback(
    private val oldList: List<ProductListItem>,
    private val newList: List<ProductListItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}