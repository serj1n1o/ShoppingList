package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.FragmentMyListsBinding
import com.bryukhanov.shoppinglist.databinding.LayoutCustomCardBinding
import com.bryukhanov.shoppinglist.databinding.LayoutCustomDialogBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.mylists.presentation.adapters.ShoppingListAdapter
import com.bryukhanov.shoppinglist.mylists.presentation.viewmodel.MyListsState
import com.bryukhanov.shoppinglist.mylists.presentation.viewmodel.MyListsViewModel
import com.bryukhanov.shoppinglist.productslist.presentation.view.ProductsListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class MyListsFragment : Fragment() {
    private var _binding: FragmentMyListsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<MyListsViewModel>()

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

        adapter = ShoppingListAdapter(object : ShoppingListAdapter.ActionListener {
            override fun onClickItem(id: Int) {
                navigateToProductScreen(id)
            }

            override fun onEdit(id: Int) {
                Toast.makeText(context, "Редактирование", Toast.LENGTH_SHORT).show()
            }

            override fun onCopy(id: Int) {
                Toast.makeText(requireContext(), "Копирование", Toast.LENGTH_SHORT).show()
            }

            override fun onDelete(id: Int) {
                Toast.makeText(requireContext(), "Удалено", Toast.LENGTH_SHORT).show()
            }
        })

        binding.rvMyLists.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyListsFragment.adapter
        }

        val itemTouchHelper = ItemTouchHelper(SwipeCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvMyLists)


        observeViewModel()

        binding.ivDelete.setOnClickListener {
            showCustomDialog()
        }

        binding.fabAdd.setOnClickListener {
            showCustomCard()
        }

        viewModel.getAllShoppingLists()
    }

    private fun navigateToProductScreen(id: Int) {
        findNavController().navigate(
            R.id.action_myListsFragment_to_productsListFragment,
            ProductsListFragment.createArgs(id)
        )
    }

    private fun observeViewModel() {
        viewModel.getListState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is MyListsState.Content -> {
                    adapter.setShoppingLists(state.myList)
                }

                MyListsState.Empty -> {
                    adapter.clearShoppingLists()
                }
            }
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
            viewModel.deleteAllShoppingLists()
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
                dialogBinding.textInputLayout.error = getString(R.string.error_hint)
            } else {
                dialogBinding.textInputLayout.error = null

                val newShoppingList = ShoppingListItem(
                    id = 0,
                    name = listName,
                    cover = R.drawable.ic_list
                )

                viewModel.addShoppingList(newShoppingList)

                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    dialogBinding.etCreateList.windowToken,
                    0
                )

                dialog.dismiss()
            }
        }

        dialog.show()

        val itemTouchHelper = ItemTouchHelper(SwipeCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvMyLists)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




