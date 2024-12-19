package com.bryukhanov.shoppinglist.core.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.LayoutCustomCardBinding

class CustomCardFragment : DialogFragment() {

    companion object {
        private const val CARD_WIDTH_RATIO = 0.75

        fun newInstance(
            theme: Int,
            title: String,
            initialText: String,
            positiveButtonText: String,
            negativeButtonText: String
        ): CustomCardFragment {
            return CustomCardFragment().apply {
                arguments = Bundle().apply {
                    putInt("theme", theme)
                    putString("title", title)
                    putString("initialText", initialText)
                    putString("positiveButtonText", positiveButtonText)
                    putString("negativeButtonText", negativeButtonText)
                }
            }
        }
    }

    var onPositiveClick: ((String) -> Unit)? = null
    var onNegativeClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val theme = arguments?.getInt("theme") ?: R.style.CustomDialogTheme
        val dialog = Dialog(requireContext(), theme)

        val binding = LayoutCustomCardBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        val params = dialog.window?.attributes
        params?.width = (resources.displayMetrics.widthPixels * CARD_WIDTH_RATIO).toInt()
        dialog.window?.attributes = params

        val title = arguments?.getString("title") ?: ""
        val initialText = arguments?.getString("initialText") ?: ""
        val positiveButtonText = arguments?.getString("positiveButtonText") ?: ""
        val negativeButtonText = arguments?.getString("negativeButtonText") ?: ""

        with(binding) {
            etCreateList.requestFocus()
            etCreateList.setText(initialText)
            etCreateList.setSelection(initialText.length)
            tvCardMessage.text = title
            btnYesCard.text = positiveButtonText
            btnNoCard.text = negativeButtonText

            if (initialText.isNotEmpty()) {
                ivCardAdd.isVisible = false
                val sizeTextPx = resources.getDimensionPixelSize(R.dimen.sixteen_size_text)
                val sizeTextSp = sizeTextPx / resources.displayMetrics.density
                tvCardMessage.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, sizeTextSp)
                tvCardMessage.gravity = androidx.core.view.GravityCompat.START
                tvCardMessage.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = resources.getDimensionPixelSize(R.dimen.six_space)
                }
            }

            btnNoCard.setOnClickListener {
                onNegativeClick?.invoke()
                closeKeyboard(etCreateList)
                dismiss()
            }

            btnYesCard.setOnClickListener {
                val listName = etCreateList.text.toString().trim()
                if (listName.isEmpty()) {
                    textInputLayout.error = getString(R.string.error_hint)
                } else {
                    textInputLayout.error = null
                    onPositiveClick?.invoke(listName)
                    closeKeyboard(etCreateList)
                    dismiss()
                }
            }

            etCreateList.doOnTextChanged { text, _, _, _ ->
                if (textInputLayout.error != null && !text.isNullOrEmpty()) {
                    textInputLayout.error = null
                }
            }
        }

        dialog.window?.decorView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                closeKeyboard(binding.etCreateList)
                dialog.dismiss()
                true
            } else {
                false
            }
        }

        return dialog
    }

    private fun closeKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
