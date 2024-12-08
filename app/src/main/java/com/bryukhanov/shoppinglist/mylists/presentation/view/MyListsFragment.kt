package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.FragmentMyListsBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.mylists.presentation.adapters.ShoppingListAdapter


class MyListsFragment : Fragment() {
    private var _binding: FragmentMyListsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShoppingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация адаптера. Фейковые данные для тестирования
        adapter = ShoppingListAdapter(
            listOf(
                ShoppingListItem(id = 1, name = "Продукты", cover = R.drawable.ic_list),
                ShoppingListItem(id = 2, name = "Для дома", cover = R.drawable.ic_list),
                ShoppingListItem(id = 3, name = "Подарки к Новому году", cover = R.drawable.ic_list)
            )
        )

        binding.rvMyLists.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyListsFragment.adapter
        }

        binding.ivDelete.setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_custom_dialog, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val messageTextView = dialogView.findViewById<TextView>(R.id.tvDialogMessage)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnNo)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnYes)

        messageTextView.text = getString(R.string.dialog_message)
        btnCancel.text = getString(R.string.dialog_cancel)
        btnConfirm.text = getString(R.string.dialog_positive_answer)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            Toast.makeText(requireContext(), "Все списки удалены", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}