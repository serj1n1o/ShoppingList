package com.bryukhanov.shoppinglist.core.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.bryukhanov.shoppinglist.databinding.LayoutCustomDialogBinding

class CustomDialog(private val context: Context) {

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
}