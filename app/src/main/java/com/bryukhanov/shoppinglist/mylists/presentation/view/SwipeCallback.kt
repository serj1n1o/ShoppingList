package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.mylists.presentation.adapters.ShoppingListAdapter

import android.animation.ObjectAnimator
import android.util.Log
import com.bryukhanov.shoppinglist.core.util.Animates

class SwipeCallback(
    private val adapter: ShoppingListAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            adapter.showActions(position)
        } else if (direction == ItemTouchHelper.RIGHT) {
            Animates.animateReset(viewHolder.itemView.findViewById(R.id.mainContainer))
            adapter.closeSwipedItem()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val buttonContainer = itemView.findViewById<View>(R.id.buttonContainer)
        val mainContainer = itemView.findViewById<View>(R.id.mainContainer)

        val buttonContainerWidth = buttonContainer.width

        val limitedDx = dX.coerceIn(-buttonContainerWidth.toFloat(), 0f)

        mainContainer.translationX = limitedDx

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            limitedDx,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}






