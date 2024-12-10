package com.bryukhanov.shoppinglist.productslist.presentation.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.databinding.ItemProductBinding

import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem

class ProductsAdapter(private val actionListener: ProductsActionListener) :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private val productList = mutableListOf<ProductListItem>()

    interface ProductsActionListener {
        val onProductClickListener: (() -> Unit)
        val onProductBoughtChangedListener: ((id: Int, isBought: Boolean) -> Unit)
    }

    fun setProductList(newList: List<ProductListItem>) {
        val diffResult = DiffUtil.calculateDiff(ProductDiffCallback(productList, newList))
        productList.clear()
        productList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearProductList() {
        setProductList(emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding = binding, actionListener = actionListener)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(productList[position])
    }


    inner class ProductsViewHolder(
        private val binding: ItemProductBinding,
        private val actionListener: ProductsActionListener,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductListItem) {
            with(binding) {
                nameProduct.text = item.name
                if (item.amount != null && item.unit != null) {
                    amountAndUnit.text = item.amount.toString() + item.unit
                }
                checkBoxProduct.setOnCheckedChangeListener { _, isChecked ->
                    actionListener.onProductBoughtChangedListener.invoke(item.id, isChecked)
                    if (isChecked) {
                        nameProduct.paintFlags =
                            nameProduct.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        nameProduct.paintFlags =
                            nameProduct.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
            }
            itemView.setOnClickListener { actionListener.onProductClickListener.invoke() }
        }
    }

}