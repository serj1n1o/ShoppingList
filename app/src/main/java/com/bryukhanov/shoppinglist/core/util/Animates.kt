package com.bryukhanov.shoppinglist.core.util

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.view.isVisible

object Animates {

    fun animateOverlay(show: Boolean, view: View, duration: Long = 300L) {
        val alpha = if (show) 1f else 0f
        view.animate()
            .alpha(alpha)
            .setDuration(duration)
            .withEndAction { if (!show) view.isVisible = false }
    }

    fun animateReset(view: View) {
        ObjectAnimator.ofFloat(view, "translationX", view.translationX, 0f).apply {
            duration = ANIMATION_DURATION
            start()
        }
    }

   private const val ANIMATION_DURATION = 300L

}