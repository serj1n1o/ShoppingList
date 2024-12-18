package com.bryukhanov.shoppinglist.productslist.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.databinding.ItemProductBinding
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem

class ProductsAdapter(private val actionListener: ProductsActionListener) :
    RecyclerView.Adapter<ProductsViewHolder>() {

    private val productList = mutableListOf<ProductListItem>()

    interface ProductsActionListener {
        val onProductClickListener: (() -> Unit)
        val onProductBoughtChangedListener: ((id: Int, isBought: Boolean) -> Unit)
        val onDeleteClick: (ProductListItem) -> Unit
        val onEditClick: (ProductListItem) -> Unit
        val onUpdateItems: (products: List<ProductListItem>) -> Unit
    }

    var isUserSortingEnabled = false

    fun isHaveBoughtProducts(): Boolean {
        return productList.any { it.isBought }
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            val movedItem = productList[fromPosition]
            for (i in fromPosition until toPosition) {
                productList[i] = productList[i + 1].copy(position = productList[i].position)
            }
            productList[toPosition] = movedItem.copy(position = productList[toPosition].position)
        } else {
            val movedItem = productList[fromPosition]
            for (i in fromPosition downTo toPosition + 1) {
                productList[i] = productList[i - 1].copy(position = productList[i].position)
            }
            productList[toPosition] = movedItem.copy(position = productList[toPosition].position)
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    fun updatePositions() {
        actionListener.onUpdateItems(productList)
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

    @SuppressLint("NotifyDataSetChanged")
    fun notifyItemAdapter() {
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding = binding, actionListener = actionListener)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(productList[position], isUserSortingEnabled)
    }


}