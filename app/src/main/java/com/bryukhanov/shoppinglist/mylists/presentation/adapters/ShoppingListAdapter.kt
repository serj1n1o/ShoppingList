package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.databinding.ItemMyListBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem

class ShoppingListAdapter(
    private val items: List<ShoppingListItem>,
    private val listener: ActionListener
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    interface ActionListener {
        fun onEdit(position: Int)
        fun onCopy(position: Int)
        fun onDelete(position: Int)
    }

    private var swipedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ItemMyListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position == swipedPosition)

        holder.binding.btnEdit.setOnClickListener { listener.onEdit(position) }
        holder.binding.btnCopy.setOnClickListener { listener.onCopy(position) }
        holder.binding.btnDelete.setOnClickListener { listener.onDelete(position) }
    }

    override fun getItemCount(): Int = items.size

    fun showActions(position: Int) {
        val previousPosition = swipedPosition
        swipedPosition = position

        // Обновляем только измененные элементы
        if (previousPosition != -1) notifyItemChanged(previousPosition)
        notifyItemChanged(swipedPosition)
    }

    inner class ShoppingListViewHolder(val binding: ItemMyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem, isSwiped: Boolean) {
            binding.tvListName.text = item.name
            binding.ivIconList.setImageResource(item.cover)

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
        }
    }
}



