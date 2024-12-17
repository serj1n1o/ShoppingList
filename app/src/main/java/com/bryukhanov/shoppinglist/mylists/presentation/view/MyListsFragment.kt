package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.core.util.CustomDialog
import com.bryukhanov.shoppinglist.core.util.SortingVariants
import com.bryukhanov.shoppinglist.databinding.FragmentMyListsBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.mylists.presentation.adapters.ShoppingListAdapter
import com.bryukhanov.shoppinglist.mylists.presentation.viewmodel.MyListsState
import com.bryukhanov.shoppinglist.mylists.presentation.viewmodel.MyListsViewModel
import com.bryukhanov.shoppinglist.productslist.presentation.view.ProductsListFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyListsFragment : Fragment() {
    private var _binding: FragmentMyListsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<MyListsViewModel>()

    private lateinit var adapter: ShoppingListAdapter
    private lateinit var searchAdapter: ShoppingListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    private var originalList: List<ShoppingListItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.groupEmptyState.visibility = View.VISIBLE

        adapter = ShoppingListAdapter(listener = object : ShoppingListAdapter.ActionListener {

            override fun onCoverChanged(item: ShoppingListItem) {
                lifecycleScope.launch {
                    viewModel.updateShoppingList(item)
                }
            }

            override fun onClickItem(myList: ShoppingListItem) {
                adapter.closeSwipedItem()
                navigateToProductScreen(myList)
            }

            override fun onEdit(myList: ShoppingListItem) {
                adapter.closeSwipedItem()
                showCustomCardEditList(myList)
            }

            override fun onCopy(listId: Int) {
                viewModel.copyShoppingList(shoppingListId = listId)
                adapter.closeSwipedItem()
            }

            override fun onDelete(myList: ShoppingListItem) {
                showCustomDialogItemDelete(myList)
                adapter.closeSwipedItem()
            }
        })

        searchAdapter = ShoppingListAdapter(listener = object : ShoppingListAdapter.ActionListener {
            override fun onCoverChanged(item: ShoppingListItem) {
                lifecycleScope.launch {
                    viewModel.updateShoppingList(item)
                }
            }

            override fun onClickItem(myList: ShoppingListItem) {
                navigateToProductScreen(myList)
                hideSearchField()
            }

            override fun onEdit(myList: ShoppingListItem) {}
            override fun onCopy(listId: Int) {}
            override fun onDelete(myList: ShoppingListItem) {}
        }, isSearchMode = true)

        binding.rvMyLists.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyListsFragment.adapter
        }

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            binding.rvSearchResults.adapter = this@MyListsFragment.searchAdapter
        }

        itemTouchHelper = ItemTouchHelper(SwipeCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvMyLists)

        observeViewModel()

        binding.ivDelete.setOnClickListener {
            adapter.closeSwipedItem()
            showCustomDialogDeleteAll()
        }

        binding.fabAdd.setOnClickListener {
            adapter.closeSwipedItem()
            showCustomCardCreateList()
        }

        binding.ivSearch.setOnClickListener {
            adapter.closeSwipedItem()
            binding.etSearch.visibility = View.VISIBLE
            binding.dimOverlay.visibility = View.VISIBLE
            binding.etSearch.requestFocus()

            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
        }

        setupSearch()

        viewModel.getAllShoppingLists()
    }

    private fun navigateToProductScreen(myList: ShoppingListItem) {
        findNavController().navigate(
            R.id.action_myListsFragment_to_productsListFragment,
            ProductsListFragment.createArgs(myList)
        )
    }


    private fun observeViewModel() {
        viewModel.getListState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is MyListsState.Content -> {
                    originalList = state.myList
                    adapter.setShoppingLists(state.myList)
                    binding.rvMyLists.visibility = View.VISIBLE
                    binding.groupEmptyState.visibility = View.GONE
                }

                MyListsState.Empty -> {
                    originalList = emptyList()
                    adapter.clearShoppingLists()
                    binding.rvMyLists.visibility = View.GONE
                    binding.groupEmptyState.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showCustomDialogDeleteAll() {
        CustomDialog(requireContext()).showConfirmDialog(
            theme = R.style.CustomDialogTheme,
            message = getString(R.string.dialog_message),
            positiveButtonText = getString(R.string.dialog_positive_answer),
            negativeButtonText = getString(R.string.dialog_cancel),
            onPositiveClick = {
                viewModel.deleteAllShoppingLists()
            },
            onNegativeClick = {}
        )
    }

    private fun showCustomDialogItemDelete(myList: ShoppingListItem) {
        CustomDialog(requireContext()).showConfirmDialog(
            theme = R.style.CustomDialogTheme,
            message = getString(R.string.dialog_message_delete_item_list, myList.name),
            positiveButtonText = getString(R.string.dialog_positive_answer),
            negativeButtonText = getString(R.string.dialog_cancel),
            onPositiveClick = {
                viewModel.deleteShoppingList(myList)
            },
            onNegativeClick = {}
        )
    }

    private fun showCustomCardCreateList() {
        CustomDialog(requireContext()).showCustomCard(
            theme = R.style.CustomDialogTheme,
            title = getString(R.string.card_message_create),
            positiveButtonText = getString(R.string.dialog_yes_card_create),
            negativeButtonText = getString(R.string.dialog_cancel),
            onNegativeClick = {},
            onPositiveClick = { name ->
                val newShoppingList = ShoppingListItem(
                    id = 0,
                    name = name,
                    cover = R.drawable.ic_list,
                    sortType = SortingVariants.USER.toString()
                )
                viewModel.addShoppingList(newShoppingList)
            }
        )
    }

    private fun showCustomCardEditList(myList: ShoppingListItem) {
        CustomDialog(requireContext()).showCustomCard(
            theme = R.style.CustomDialogTheme,
            title = getString(R.string.card_message_edit),
            initialText = myList.name,
            positiveButtonText = getString(R.string.dialog_yes_card_edit),
            negativeButtonText = getString(R.string.dialog_cancel),
            onNegativeClick = {},
            onPositiveClick = { name ->
                val newShoppingList = myList.copy(name = name)
                viewModel.updateShoppingList(newShoppingList)
            }
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearch() {
        val etSearch = binding.etSearch
        val icBackArrow = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back_arrow_list)
        val icClear = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)

        etSearch.setCompoundDrawablesWithIntrinsicBounds(icBackArrow, null, null, null)

        etSearch.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                etSearch.setCompoundDrawablesWithIntrinsicBounds(
                    icBackArrow,
                    null,
                    icClear,
                    null
                )
                filterLists(text.toString())
            } else {
                etSearch.setCompoundDrawablesWithIntrinsicBounds(icBackArrow, null, null, null)
                hideSearchResults()
            }
        }

        etSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableStart = etSearch.compoundDrawables[0]
                val drawableEnd = etSearch.compoundDrawables[2]

                if (drawableStart != null && event.rawX <= (etSearch.left + drawableStart.bounds.width())) {
                    hideSearchField()
                    return@setOnTouchListener true
                }

                if (drawableEnd != null && event.rawX >= (etSearch.right - drawableEnd.bounds.width())) {
                    etSearch.text.clear()
                    showMyListsWithOverlay()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun showMyListsWithOverlay() {
        binding.rvSearchResults.visibility = View.GONE
        binding.layoutSearchNotFoundContainer.visibility = View.GONE
        binding.rvMyLists.visibility = View.VISIBLE
        binding.dimOverlay.visibility = View.VISIBLE
        binding.searchDivider.visibility = View.VISIBLE
        binding.etSearch.visibility = View.VISIBLE
    }

    private fun filterLists(query: String) {
        if (query.isEmpty()) {
            searchAdapter.setShoppingLists(emptyList())
            binding.rvSearchResults.visibility = View.GONE
            binding.rvMyLists.visibility = View.VISIBLE
            binding.groupEmptyState.visibility =
                if (originalList.isEmpty()) View.VISIBLE else View.GONE
            binding.dimOverlay.visibility = View.VISIBLE
            binding.layoutSearchNotFoundContainer.visibility = View.VISIBLE
        } else {
            val filteredList = originalList.filter {
                it.name.startsWith(query, ignoreCase = true)
            }

            searchAdapter.setShoppingLists(filteredList)

            binding.rvMyLists.visibility = View.GONE
            binding.groupEmptyState.visibility = View.GONE

            if (filteredList.isNotEmpty()) {
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.layoutSearchNotFoundContainer.visibility = View.GONE
                binding.searchDivider.visibility = View.VISIBLE
                binding.dimOverlay.visibility = View.GONE
            } else {
                binding.rvSearchResults.visibility = View.GONE
                binding.layoutSearchNotFoundContainer.visibility = View.VISIBLE
                binding.searchDivider.visibility = View.VISIBLE
                binding.dimOverlay.visibility = View.GONE
            }
        }
    }


    private fun hideSearchResults() {
        binding.rvSearchResults.visibility = View.GONE
        binding.rvMyLists.visibility = View.VISIBLE
        binding.groupEmptyState.visibility = if (originalList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun hideSearchField() {
        binding.etSearch.visibility = View.GONE
        binding.dimOverlay.visibility = View.GONE
        binding.etSearch.text.clear()
        binding.etSearch.clearFocus()
        binding.rvSearchResults.visibility = View.GONE
        binding.layoutSearchNotFoundContainer.visibility = View.GONE
        binding.searchDivider.visibility = View.GONE
        binding.rvMyLists.visibility = View.VISIBLE
        binding.groupEmptyState.visibility = if (originalList.isEmpty()) View.VISIBLE else View.GONE

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





