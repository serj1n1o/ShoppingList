package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.mylists.presentation.adapters.ShoppingListAdapter

class SwipeCallback(
    private val adapter: ShoppingListAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        adapter.showActions(position)
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
        val limitedDx = dX.coerceAtLeast(-buttonContainerWidth.toFloat())

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

