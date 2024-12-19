package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.ItemMyListBinding
import com.bryukhanov.shoppinglist.databinding.ItemMyListSearchBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShoppingListAdapter(
    private val listener: ActionListener,
    private val isSearchMode: Boolean = false,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val shoppingLists = mutableListOf<ShoppingListItem>()

    interface ActionListener {
        fun onCoverChanged(item: ShoppingListItem)
        fun onClickItem(myList: ShoppingListItem)
        fun onEdit(myList: ShoppingListItem)
        fun onCopy(listId: Int)
        fun onDelete(myList: ShoppingListItem)
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
            is ShoppingListViewHolder -> holder.bind(item)

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

    inner class ShoppingListViewHolder(
        private val binding: ItemMyListBinding,
        private val listener: ActionListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem) {
            item.cover?.let { binding.ivIconList.setImageResource(it) }

            binding.tvListName.text = item.name
            binding.ivIconList.setOnClickListener {
                showIconPickerDialog(item)
            }

            itemView.setOnClickListener { listener.onClickItem(item) }
            binding.btnEdit.setOnClickListener { listener.onEdit(item) }
            binding.btnCopy.setOnClickListener { listener.onCopy(item.id) }
            binding.btnDelete.setOnClickListener { listener.onDelete(item) }
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
                R.drawable.ic_icon10,
                R.drawable.ic_icon11,
                R.drawable.ic_icon12,
                R.drawable.ic_icon13,
                R.drawable.ic_icon14,
                R.drawable.ic_icon15,
                R.drawable.ic_icon16,
                R.drawable.ic_icon17,
                R.drawable.ic_icon18,
                R.drawable.ic_icon19,
                R.drawable.ic_icon20,
                R.drawable.ic_icon21,
                R.drawable.ic_icon22,
                R.drawable.ic_icon23,
                R.drawable.ic_icon24,
                R.drawable.ic_icon25,
                R.drawable.ic_icon26,
                R.drawable.ic_icon27,
                R.drawable.ic_icon28,
                R.drawable.ic_icon29
            )
        }
    }

    inner class SearchListViewHolder(
        private val binding: ItemMyListSearchBinding,
        private val listener: ActionListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShoppingListItem) {
            item.cover?.let { binding.ivIconListSearch.setImageResource(it) }
            binding.tvListNameSearch.text = item.name
            itemView.setOnClickListener { listener.onClickItem(item) }
        }
    }

}







