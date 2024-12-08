package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.databinding.ItemMyListBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem

class ShoppingListAdapter(private val shoppingLists: MutableList<ShoppingListItem>) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    inner class ShoppingListViewHolder(private val binding: ItemMyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem) {
            if (item.cover != null) {
                binding.ivIconList.setImageResource(item.cover)
            }
            binding.tvListName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ItemMyListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        holder.bind(shoppingLists[position])
    }

    override fun getItemCount(): Int = shoppingLists.size

    fun clearShoppingLists() {
        shoppingLists.clear()
        notifyDataSetChanged()
    }
}
