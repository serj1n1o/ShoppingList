package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.databinding.ItemMyListBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem

class ShoppingListAdapter(
    private val shoppingLists: List<ShoppingListItem>,
    private val clickListener: ClickListener,
) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    inner class ShoppingListViewHolder(
        private val binding: ItemMyListBinding,
        private val clickListener: ClickListener,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem) {
            if (item.cover != null) {
                binding.ivIconList.setImageResource(item.cover)
            }
            binding.tvListName.text = item.name
            itemView.setOnClickListener { clickListener.onClick.invoke(item.id) }
        }
    }

    interface ClickListener {
        val onClick: ((id: Int) -> Unit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ItemMyListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShoppingListViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        holder.bind(shoppingLists[position])
    }

    override fun getItemCount(): Int = shoppingLists.size
}
