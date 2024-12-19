package com.bryukhanov.shoppinglist.core.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.LayoutCustomDialogBinding

class CustomDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(
            theme: Int,
            message: String,
            positiveButtonText: String,
            negativeButtonText: String
        ): CustomDialogFragment {
            return CustomDialogFragment().apply {
                arguments = bundleOf(
                    "theme" to theme,
                    "message" to message,
                    "positiveButtonText" to positiveButtonText,
                    "negativeButtonText" to negativeButtonText
                )
            }
        }
    }

    var onPositiveClick: (() -> Unit)? = null
    var onNegativeClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val theme = arguments?.getInt("theme") ?: R.style.CustomDialogTheme
        val dialog = Dialog(requireContext(), theme)
        val binding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        binding.tvDialogMessage.text = arguments?.getString("message")
        binding.btnYes.text = arguments?.getString("positiveButtonText")
        binding.btnNo.text = arguments?.getString("negativeButtonText")

        binding.btnYes.setOnClickListener {
            onPositiveClick?.invoke()
            dismiss()
        }

        binding.btnNo.setOnClickListener {
            onNegativeClick?.invoke()
            dismiss()
        }

        return dialog
    }
}