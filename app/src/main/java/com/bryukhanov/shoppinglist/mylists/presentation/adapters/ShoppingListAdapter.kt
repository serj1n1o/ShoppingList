package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.core.util.Animates
import com.bryukhanov.shoppinglist.databinding.ItemMyListBinding
import com.bryukhanov.shoppinglist.databinding.ItemMyListSearchBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShoppingListAdapter(
    private val listener: ActionListener,
    private val isSearchMode: Boolean = false
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val shoppingLists = mutableListOf<ShoppingListItem>()
    private var swipedPosition: Int = -1

    interface ActionListener {
        fun onCoverChanged(item: ShoppingListItem)
        fun onClickItem(myList: ShoppingListItem)
        fun onEdit(id: Int)
        fun onCopy(id: Int)
        fun onDelete(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isSearchMode) {
            val binding = ItemMyListSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            SearchListViewHolder(binding, listener)
        } else {
            val binding = ItemMyListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShoppingListViewHolder(binding, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = shoppingLists[position]
        when (holder) {
            is ShoppingListViewHolder -> holder.bind(item, position == swipedPosition)

            is SearchListViewHolder -> holder.bind(item)

        }
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

    inner class ShoppingListViewHolder(
        private val binding: ItemMyListBinding,
        private val listener: ActionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem, isSwiped: Boolean) {
            item.cover?.let { binding.ivIconList.setImageResource(it) }
            binding.tvListName.text = item.name

            binding.ivIconList.setOnClickListener {
                showIconPickerDialog(item)
            }

            itemView.setOnClickListener {
                if (isSwiped) {
                    Animates.animateReset(binding.mainContainer)
                    closeSwipedItem()
                } else {
                    listener.onClickItem(item)
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

        private fun showIconPickerDialog(item: ShoppingListItem) {
            val dialog = BottomSheetDialog(itemView.context)
            val view =
                LayoutInflater.from(itemView.context).inflate(R.layout.dialog_icon_picker, null)
            dialog.setContentView(view)

            val iconRecyclerView = view.findViewById<RecyclerView>(R.id.rvIcons)
            val icons = getIconsList()

            val adapter = IconPickerAdapter(icons) { selectedIconResId ->
                item.cover = selectedIconResId
                binding.ivIconList.setImageResource(selectedIconResId)
                listener.onCoverChanged(item)
                dialog.dismiss()
            }

            iconRecyclerView.adapter = adapter
            iconRecyclerView.layoutManager = GridLayoutManager(itemView.context, 5)

            dialog.show()
        }

        private fun getIconsList(): List<Int> {
            return listOf(
                R.drawable.ic_list,
                R.drawable.ic_icon1,
                R.drawable.ic_icon2,
                R.drawable.ic_icon3,
                R.drawable.ic_icon4,
                R.drawable.ic_icon5,
                R.drawable.ic_icon6,
                R.drawable.ic_icon7,
                R.drawable.ic_icon8,
                R.drawable.ic_icon9,
                R.drawable.ic_icon10
            )
        }
    }

    inner class SearchListViewHolder(
        private val binding: ItemMyListSearchBinding,
        private val listener: ActionListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingListItem) {
            binding.tvListNameSearch.text = item.name
            itemView.setOnClickListener { listener.onClickItem(item) }
        }
    }

}







