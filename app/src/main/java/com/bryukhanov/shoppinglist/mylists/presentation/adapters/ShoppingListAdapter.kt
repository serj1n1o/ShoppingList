package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: ShoppingListItem, isSwiped: Boolean) {

            item.cover?.let { binding.ivIconList.setImageResource(it) }
            binding.tvListName.text = item.name

            itemView.setOnClickListener { listener.onClickItem(item.id) }

            // Управляем видимостью кнопок и смещением контейнера
            if (isSwiped) {
                binding.buttonContainer.visibility = View.VISIBLE
                binding.mainContainer.translationX = -binding.buttonContainer.width.toFloat()
            } else {
                binding.buttonContainer.visibility = View.GONE
                binding.mainContainer.translationX = 0f
            }

            // Принудительное обновление размеров кнопок
            binding.buttonContainer.post {
                if (isSwiped) {
                    binding.mainContainer.translationX = -binding.buttonContainer.width.toFloat()
                }
            }

            binding.btnEdit.setOnClickListener {
                listener.onEdit(item.id)
            }
            binding.btnCopy.setOnClickListener {
                listener.onCopy(item.id)
            }
            binding.btnDelete.setOnClickListener {
                listener.onDelete(item.id)
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

        // Обновляем только измененные элементы
        if (previousPosition != -1) notifyItemChanged(previousPosition)
        notifyItemChanged(swipedPosition)
    }
}






