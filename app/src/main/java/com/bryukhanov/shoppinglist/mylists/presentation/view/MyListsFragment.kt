package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.FragmentMyListsBinding
import com.bryukhanov.shoppinglist.databinding.LayoutCustomCardBinding
import com.bryukhanov.shoppinglist.databinding.LayoutCustomDialogBinding
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

        adapter = ShoppingListAdapter()

        binding.rvMyLists.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyListsFragment.adapter
        }

        //  Фейковые данные для тестирования
        adapter.setShoppingLists(
            listOf(
                ShoppingListItem(id = 1, name = "Продукты", cover = R.drawable.ic_list),
                ShoppingListItem(id = 2, name = "Для дома", cover = R.drawable.ic_list),
                ShoppingListItem(id = 3, name = "Подарки к Новому году", cover = R.drawable.ic_list)
            )
        )

        binding.ivDelete.setOnClickListener {
            showCustomDialog()
        }

        binding.fabAdd.setOnClickListener {
            showCustomCard()
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext(), R.style.CustomDialogTheme)
        val dialogBinding =
            LayoutCustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvDialogMessage.text = getString(R.string.dialog_message)
        dialogBinding.btnNo.text = getString(R.string.dialog_cancel)
        dialogBinding.btnYes.text = getString(R.string.dialog_positive_answer)

        dialogBinding.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnYes.setOnClickListener {
            adapter.clearShoppingLists()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showCustomCard() {
        val dialog = Dialog(requireContext(), R.style.CustomDialogTheme)
        val dialogBinding = LayoutCustomCardBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)

        dialogBinding.etCreateList.requestFocus()

        dialogBinding.btnNoCard.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnYesCard.setOnClickListener {
            val listName = dialogBinding.etCreateList.text.toString().trim()

            if (listName.isEmpty()) {
                dialogBinding.textInputLayout.error = "Название не может быть пустым"
            } else {
                dialogBinding.textInputLayout.error = null

                // Создаем новый элемент списка
                val newShoppingList = ShoppingListItem(
                    id = generateId(),
                    name = listName,
                    cover = R.drawable.ic_list
                )

                adapter.setShoppingLists(adapter.getShoppingLists() + newShoppingList)

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun generateId(): Int {
        return adapter.itemCount + 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}