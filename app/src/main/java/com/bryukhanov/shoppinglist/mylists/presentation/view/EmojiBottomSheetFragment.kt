package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.emoji2.emojipicker.EmojiPickerView
import com.bryukhanov.shoppinglist.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmojiBottomSheetFragment(
    private val onEmojiSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val emojiPickerView = EmojiPickerView(requireContext())

        emojiPickerView.setOnEmojiPickedListener { emoji ->
            onEmojiSelected(emoji.emoji)
            dismiss()
        }
        emojiPickerView.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bottom_sheet_background)
        return emojiPickerView
    }
}