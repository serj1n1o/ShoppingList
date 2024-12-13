package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.core.util.Animates
import com.bryukhanov.shoppinglist.databinding.ItemMyListBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem

class ShoppingListAdapter(private val listener: ActionListener) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private val shoppingLists = mutableListOf<ShoppingListItem>()
    private var swipedPosition: Int = -1

    interface ActionListener {
        fun onClickItem(id: Int)
        fun onEdit(id: Int)
        fun onCopy(id: Int)
        fun onDelete(id: Int)
    }

    inner class ShoppingListViewHolder(
        private val binding: ItemMyListBinding,
        private val listener: ActionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem, isSwiped: Boolean) {
            item.cover?.let { binding.ivIconList.setImageResource(it) }
            binding.tvListName.text = item.name

            itemView.setOnClickListener {
                if (isSwiped) {
                    Animates.animateReset(binding.mainContainer)
                    closeSwipedItem()
                } else {
                    listener.onClickItem(item.id)
                    closeSwipedItem()
                }
            }

            if (!isSwiped) {
                binding.mainContainer.translationX = 0f
                binding.buttonContainer.visibility = View.GONE
            } else {
                binding.buttonContainer.visibility = View.VISIBLE
            }

            binding.btnEdit.setOnClickListener {
                listener.onEdit(item.id)
                Animates.animateReset(binding.mainContainer)
                closeSwipedItem()
            }
            binding.btnCopy.setOnClickListener {
                listener.onCopy(item.id)
                Animates.animateReset(binding.mainContainer)
                closeSwipedItem()
            }
            binding.btnDelete.setOnClickListener {
                listener.onDelete(item.id)
                Animates.animateReset(binding.mainContainer)
                closeSwipedItem()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ItemMyListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShoppingListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val item = shoppingLists[position]
        holder.bind(item, position == swipedPosition)
    }

    override fun getItemCount(): Int = shoppingLists.size

    fun setShoppingLists(newShoppingLists: List<ShoppingListItem>) {
        shoppingLists.clear()
        shoppingLists.addAll(newShoppingLists)
        notifyDataSetChanged()
    }

    fun clearShoppingLists() {
        shoppingLists.clear()
        notifyDataSetChanged()
    }

    fun showActions(position: Int) {
        val previousPosition = swipedPosition
        swipedPosition = position

        if (previousPosition != -1) notifyItemChanged(previousPosition)
        notifyItemChanged(swipedPosition)
    }

    fun closeSwipedItem() {
        val previousPosition = swipedPosition
        swipedPosition = -1
        if (previousPosition != -1) notifyItemChanged(previousPosition)
    }

}







