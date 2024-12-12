package com.bryukhanov.shoppinglist.productslist.presentation.adapters

import android.graphics.Paint
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.ItemProductBinding
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import com.bryukhanov.shoppinglist.productslist.presentation.adapters.ProductsAdapter.ProductsActionListener

class ProductsViewHolder(
    private val binding: ItemProductBinding,
    private val actionListener: ProductsActionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProductListItem) {
        with(binding) {
            nameProduct.text = item.name
            if (item.amount != null && item.unit != null) {
                amountAndUnit.text = buildString {
                    append(item.amount)
                    append(" ")
                    append(item.unit)
                }
            } else {
                amountAndUnit.isVisible = false
            }
            if (item.isBought) {
                nameProduct.paintFlags = nameProduct.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                checkBoxProduct.isChecked = true
            }

            checkBoxProduct.setOnCheckedChangeListener { _, isChecked ->
                actionListener.onProductBoughtChangedListener.invoke(item.id, isChecked)
                if (isChecked) {
                    nameProduct.paintFlags =
                        nameProduct.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    checkBoxProduct.setButtonDrawable(R.drawable.ic_checkbox_checked)
                } else {
                    nameProduct.paintFlags =
                        nameProduct.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    checkBoxProduct.setButtonDrawable(R.drawable.ic_checkbox_unchecked)
                }
            }
        }
        itemView.setOnClickListener { actionListener.onProductClickListener.invoke() }
    }

}