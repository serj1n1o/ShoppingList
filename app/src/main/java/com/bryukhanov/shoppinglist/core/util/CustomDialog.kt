package com.bryukhanov.shoppinglist.core.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.LayoutCustomCardBinding
import com.bryukhanov.shoppinglist.databinding.LayoutCustomDialogBinding

class CustomDialog(private val context: Context) {

    companion object {
        private const val CARD_WIDTH_RATIO = 0.75
    }

    fun showConfirmDialog(
        theme: Int,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit,
    ) {
        val dialog = Dialog(context, theme)
        val dialogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvDialogMessage.text = message
        dialogBinding.btnNo.text = negativeButtonText
        dialogBinding.btnYes.text = positiveButtonText

        dialogBinding.btnNo.setOnClickListener {
            onNegativeClick()
            dialog.dismiss()
        }

        dialogBinding.btnYes.setOnClickListener {
            onPositiveClick()
            dialog.dismiss()
        }

        dialog.show()
    }


    @SuppressLint("ClickableViewAccessibility")
    fun showCustomCard(
        theme: Int,
        title: String,
        initialText: String = "",
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveClick: (String) -> Unit,
        onNegativeClick: () -> Unit,
    ) {
        val dialog = Dialog(context, theme)
        val dialogBinding = LayoutCustomCardBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)

        val params = dialog.window?.attributes
        params?.width = (context.resources.displayMetrics.widthPixels * CARD_WIDTH_RATIO).toInt()
        dialog.window?.attributes = params

        dialog.window?.decorView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                closeKeyboard(dialogBinding.etCreateList)
                dialog.dismiss()
                true
            } else {
                false
            }
        }

        with(dialogBinding) {
            etCreateList.requestFocus()
            etCreateList.setText(initialText)
            etCreateList.setSelection(initialText.length)
            tvCardMessage.text = title
            if (initialText.isNotEmpty()) {
                ivCardAdd.isVisible = false
                val sizeTextPx = context.resources.getDimensionPixelSize(R.dimen.sixteen_size_text)
                val sizeTextSp = sizeTextPx / context.resources.displayMetrics.density
                tvCardMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeTextSp)
                tvCardMessage.gravity = GravityCompat.START
            }
            btnNoCard.text = negativeButtonText
            btnYesCard.text = positiveButtonText

            btnNoCard.setOnClickListener {
                onNegativeClick()
                closeKeyboard(etCreateList)
                dialog.dismiss()
            }

            etCreateList.doOnTextChanged { text, _, _, _ ->
                if (textInputLayout.error != null && !text.isNullOrEmpty()) {
                    textInputLayout.error = null
                }
            }

            btnYesCard.setOnClickListener {
                val listName = etCreateList.text.toString().trim()
                if (listName.isEmpty()) {
                    textInputLayout.error = context.getString(R.string.error_hint)
                } else {
                    textInputLayout.error = null
                    onPositiveClick(listName)
                    closeKeyboard(etCreateList)
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}