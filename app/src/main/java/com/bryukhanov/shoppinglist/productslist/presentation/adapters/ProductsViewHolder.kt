package com.bryukhanov.shoppinglist.productslist.presentation.adapters

import android.graphics.Paint
import androidx.core.content.ContextCompat
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

        binding.checkBoxProduct.setOnCheckedChangeListener { buttonView, isChecked ->
            actionListener.onProductBoughtChangedListener(item.id, isChecked)
            if (isChecked) {
                buttonView.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_checkbox_checked)
                binding.nameProduct.paintFlags =
                    binding.nameProduct.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                buttonView.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_checkbox_unchecked)
                binding.nameProduct.paintFlags =
                    binding.nameProduct.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

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
                checkBoxProduct.isChecked = true
            } else {
                checkBoxProduct.isChecked = false
            }

        }
        itemView.setOnClickListener { actionListener.onProductClickListener.invoke() }
    }

}