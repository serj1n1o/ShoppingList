package com.bryukhanov.shoppinglist.core.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.LayoutCustomDialogBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.mylists.presentation.viewmodel.CustomDialogListener

class CustomDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(
            theme: Int,
            message: String,
            positiveButtonText: String,
            negativeButtonText: String,
            item: ShoppingListItem? = null
        ): CustomDialogFragment {
            return CustomDialogFragment().apply {
                arguments = bundleOf(
                    "theme" to theme,
                    "message" to message,
                    "positiveButtonText" to positiveButtonText,
                    "negativeButtonText" to negativeButtonText,
                    "item" to item
                )
            }
        }
    }

    private var listener: CustomDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? CustomDialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val theme = arguments?.getInt("theme") ?: R.style.CustomDialogTheme
        val dialog = Dialog(requireContext(), theme)

        val binding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        binding.tvDialogMessage.text = arguments?.getString("message")
        binding.btnYes.text = arguments?.getString("positiveButtonText")
        binding.btnNo.text = arguments?.getString("negativeButtonText")

        binding.btnYes.setOnClickListener {
            listener?.onPositiveClick()
            dismiss()
        }

        binding.btnNo.setOnClickListener {
            listener?.onNegativeClick()
            dismiss()
        }

        return dialog
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}

