package com.bryukhanov.shoppinglist.core.util

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION
import androidx.recyclerview.widget.RecyclerView
import com.bryukhanov.shoppinglist.productslist.presentation.adapters.ProductsAdapter


fun setItemTouchHelperProducts(
    recyclerView: RecyclerView,
    containerId: Int,
    adapter: ProductsAdapter,
) {

    ItemTouchHelper(object : ItemTouchHelper.Callback() {

        private var limitScrollX = 0
        private var currentScrollX = 0
        private var currentScrollXWhenInActive = 0
        private var initXWhenInActive = 0f
        private var firstInActive = false
        var leftSwipeChecker = false


        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
        ): Int {
            val dragFlags = 0
            val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return Integer.MAX_VALUE.toFloat()
        }

        override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
            return Integer.MAX_VALUE.toFloat()
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean,
        ) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                if (limitScrollX == 0) {
                    val container = viewHolder.itemView.findViewById<View>(containerId)
                    limitScrollX = container.width
                }

                if (viewHolder.itemView.scrollX == 0) {
                    leftSwipeChecker = true
                }

                leftSwipeChecker = leftSwipeChecker && dX < 0

                if (leftSwipeChecker) {
                    recoverSwipedItem(viewHolder, recyclerView)
                    if (viewHolder.itemView.scrollX != 0) {
                        leftSwipeChecker = false
                    }
                }

                if (dX == 0f) {
                    currentScrollX = viewHolder.itemView.scrollX
                    firstInActive = true
                }

                if (isCurrentlyActive) {
                    var scrollOffset = currentScrollX + (-dX).toInt()
                    if (scrollOffset > limitScrollX) {
                        scrollOffset = limitScrollX
                    } else if (scrollOffset < 0) {
                        scrollOffset = 0
                    }
                    viewHolder.itemView.scrollTo(scrollOffset, 0)
                } else {
                    if (firstInActive) {
                        firstInActive = false
                        currentScrollXWhenInActive = viewHolder.itemView.scrollX
                        initXWhenInActive = dX
                    }

                    if (viewHolder.itemView.scrollX < limitScrollX) {
                        viewHolder.itemView.scrollTo(
                            (currentScrollXWhenInActive * dX / initXWhenInActive).toInt(),
                            0
                        )
                    }
                }
            }
        }

        private fun recoverSwipedItem(
            viewHolder: RecyclerView.ViewHolder,
            recyclerView: RecyclerView,
        ) {

            for (i in adapter.itemCount downTo 0) {
                val itemView = recyclerView.findViewHolderForAdapterPosition(i)?.itemView

                if (i != viewHolder.bindingAdapterPosition) {

                    itemView?.let {
                        if (it.scrollX > 0) {
                            smoothScrollTo(itemView, it.scrollX, 0)
                        }
                    }
                }
            }
        }

        private fun smoothScrollTo(itemView: View, from: Int, to: Int) {
            ValueAnimator.ofInt(from, to).apply {
                duration = DEFAULT_SWIPE_ANIMATION_DURATION.toLong()
                addUpdateListener {
                    itemView.scrollTo(it.animatedValue as Int, 0)
                }
                start()
            }
        }


        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
        ) {
            super.clearView(recyclerView, viewHolder)
            if (viewHolder.itemView.scrollX > limitScrollX) {
                viewHolder.itemView.scrollTo(limitScrollX, 0)

            } else if (viewHolder.itemView.scrollX < 0) {
                viewHolder.itemView.scrollTo(0, 0)
            }
        }

    }).apply {
        attachToRecyclerView(recyclerView)
    }

}

fun resetAllItemsScroll(recyclerView: RecyclerView) {
    val childCount = recyclerView.childCount
    for (i in 0 until childCount) {
        val child = recyclerView.getChildAt(i)
        ValueAnimator.ofInt(child.scrollX, 0).apply {
            duration = DEFAULT_SWIPE_ANIMATION_DURATION.toLong()
            addUpdateListener {
                child.scrollTo(it.animatedValue as Int, 0)
            }
            start()
        }
    }
}
