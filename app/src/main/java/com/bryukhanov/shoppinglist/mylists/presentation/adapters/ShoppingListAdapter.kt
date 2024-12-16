package com.bryukhanov.shoppinglist.mylists.presentation.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.core.util.Animates
import com.bryukhanov.shoppinglist.databinding.ItemMyListBinding
import com.bryukhanov.shoppinglist.databinding.ItemMyListSearchBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.mylists.presentation.view.EmojiBottomSheetFragment

class ShoppingListAdapter(
    private val listener: ActionListener,
    private val isSearchMode: Boolean = false
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val shoppingLists = mutableListOf<ShoppingListItem>()
    private var swipedPosition: Int = -1

    interface ActionListener {
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
                val bottomSheet = EmojiBottomSheetFragment { emoji ->
                    val emojiBitmap = emojiToBitmap(itemView.context, emoji, sizeInDp = 36f)
                    binding.ivIconList.setImageBitmap(emojiBitmap)
                }
                bottomSheet.show(
                    (itemView.context as AppCompatActivity).supportFragmentManager,
                    "emojiBottomSheet"
                )
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
    }

    fun emojiToBitmap(context: Context, emoji: String, sizeInDp: Float = 40f): Bitmap {
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, sizeInDp, context.resources.displayMetrics
        ).toInt()

        val paint = Paint().apply {
            textSize = sizeInPx * 0.8f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val bitmap = Bitmap.createBitmap(sizeInPx, sizeInPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val baseline = (sizeInPx / 2) - (paint.descent() + paint.ascent()) / 2
        canvas.drawText(emoji, sizeInPx / 2f, baseline, paint)

        return bitmap
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







