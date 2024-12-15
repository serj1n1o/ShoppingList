package com.bryukhanov.shoppinglist.productslist.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.core.util.Animates
import com.bryukhanov.shoppinglist.core.util.CustomDialog
import com.bryukhanov.shoppinglist.core.util.SortingVariants
import com.bryukhanov.shoppinglist.core.util.Units
import com.bryukhanov.shoppinglist.databinding.FragmentProductsListBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.productslist.domain.models.ProductListItem
import com.bryukhanov.shoppinglist.productslist.presentation.adapters.ProductsAdapter
import com.bryukhanov.shoppinglist.productslist.presentation.viewmodel.ProductsState
import com.bryukhanov.shoppinglist.productslist.presentation.viewmodel.ProductsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsListFragment : Fragment() {

    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ProductsViewModel>()

    private var bottomSheetAddProduct: BottomSheetBehavior<ConstraintLayout>? = null
    private var bottomSheetMenu: BottomSheetBehavior<ConstraintLayout>? = null

    private var nameProduct: String? = null
    private var amountProduct: Int? = null
    private var unitProduct: String? = null

    private val productsAdapter by lazy {
        ProductsAdapter(object : ProductsAdapter.ProductsActionListener {
            override val onProductClickListener: () -> Unit
                get() = { Toast.makeText(requireContext(), "CLICK", Toast.LENGTH_SHORT).show() }
            override val onProductBoughtChangedListener: (Int, Boolean) -> Unit
                get() = { id, isBought ->
                    viewModel.updateProductBoughtStatus(id, isBought)
                }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheet()

        val shoppingList = BundleCompat.getParcelable(
            requireArguments(),
            KEY_PRODUCT_LIST,
            ShoppingListItem::class.java
        ) as ShoppingListItem

        binding.txtProducts.text = shoppingList.name

        val unitsAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item_layout_unit, Units.entries)
        binding.completeTextUnit.setAdapter(unitsAdapter)

        viewModel.getProducts(shoppingList.id)

        binding.rvProducts.adapter = productsAdapter

        viewModel.getProductState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProductsState.Content -> showContent(state.productList)
                ProductsState.Empty -> showEmptyPlaceHolder()
            }
        }

        viewModel.getSelectedSorting().observe(viewLifecycleOwner) { sort ->
            when (sort!!) {
                SortingVariants.ALPHABET -> {
                    binding.sortLayout.typeSort.text = SortingVariants.ALPHABET.toString()
                    viewModel.sortProducts(sort)
                }

                SortingVariants.USER -> {
                    binding.sortLayout.typeSort.text = SortingVariants.USER.toString()
                    viewModel.sortProducts(sort)
                }
            }
        }

        binding.fabAddProduct.setOnClickListener {
            bottomSheetAddProduct?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetAddProduct?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        swapImageFab(state = BottomSheetBehavior.STATE_COLLAPSED)
                        showOverlay(true)
                        binding.fabAddProduct.setOnClickListener {
                            createProduct(shoppingListId = shoppingList.id, view = it)
                        }
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        showOverlay(false)
                        binding.fabAddProduct.setOnClickListener {
                            bottomSheetAddProduct?.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                        swapImageFab(state = BottomSheetBehavior.STATE_HIDDEN)
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

        with(binding) {

            completeTextUnit.setOnItemClickListener { _, _, position, _ ->
                unitProduct = Units.entries[position].toString()
            }
        }

        with(binding.editTextNameProduct) {
            doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    nameProduct = text.toString()
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(this)
                    true
                } else {
                    false
                }
            }
        }

        with(binding.editTextAmountProduct) {
            doOnTextChanged { text, _, _, _ ->
                amountProduct = if (!text.isNullOrEmpty()) {
                    text.toString().toInt()
                } else null
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(this)
                    true
                } else {
                    false
                }
            }
        }

        binding.minusUnit.setOnClickListener {
            if (amountProduct != null) {
                amountProduct = amountProduct!! - 1
                binding.editTextAmountProduct.setText(amountProduct.toString())
                if (amountProduct == 0) {
                    amountProduct = null
                    binding.editTextAmountProduct.text?.clear()
                }
            }
        }

        binding.plusUnit.setOnClickListener {
            if (amountProduct == null) {
                amountProduct = 1
                binding.editTextAmountProduct.setText(amountProduct.toString())
            } else {
                amountProduct = amountProduct!! + 1
                binding.editTextAmountProduct.setText(amountProduct.toString())
            }

        }

        binding.ivBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivMenu.setOnClickListener {
            bottomSheetMenu?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetMenu?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        showOverlay(true)
                        binding.fabAddProduct.isVisible = false
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        showOverlay(false)
                        binding.fabAddProduct.isVisible = true
                    }

                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        binding.completeTextUnit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val viewComplete = binding.completeTextUnit
                hideKeyboard(viewComplete)
                viewComplete.post { viewComplete.showDropDown() }
            }
        }

        binding.deleteAllProductsMenu.setOnClickListener {
            bottomSheetMenu?.state = BottomSheetBehavior.STATE_HIDDEN
            if (productsAdapter.itemCount > 0) {
                CustomDialog(requireContext()).showConfirmDialog(
                    theme = R.style.CustomDialogTheme,
                    message = getString(R.string.dialog_message_delete_all_product),
                    positiveButtonText = getString(R.string.dialog_positive_answer),
                    negativeButtonText = getString(R.string.dialog_cancel),
                    onPositiveClick = {
                        viewModel.deleteAllProduct(shoppingList.id)
                    },
                    onNegativeClick = {}
                )
            }
        }

        binding.clearBoughtMenu.setOnClickListener {
            bottomSheetMenu?.state = BottomSheetBehavior.STATE_HIDDEN
            if (productsAdapter.itemCount > 0) {
                CustomDialog(requireContext()).showConfirmDialog(
                    theme = R.style.CustomDialogTheme,
                    message = getString(R.string.dialog_message_delete_bought_product),
                    positiveButtonText = getString(R.string.dialog_positive_answer),
                    negativeButtonText = getString(R.string.dialog_cancel),
                    onPositiveClick = {
                        viewModel.deleteBoughtProduct(shoppingList.id)
                    },
                    onNegativeClick = {}
                )
            }
        }

        binding.overlay.setOnClickListener {
            bottomSheetMenu?.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetAddProduct?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.sortLayout.root.setOnClickListener {
            showPopupWindow(binding.sortLayout.root)
        }

    }

    private fun showPopupWindow(anchor: View) {
        val popupView = layoutInflater.inflate(R.layout.dropdown_sorting_select_layout, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val checkboxAlphabet = popupView.findViewById<CheckBox>(R.id.checkBoxAlphabetSort)
        val checkboxUser = popupView.findViewById<CheckBox>(R.id.checkBoxUserSort)

        checkboxAlphabet.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_checkbox_checked)
                checkboxUser.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_checkbox_unchecked)
            } else {
                buttonView.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_checkbox_unchecked)
            }
        }

        checkboxUser.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_checkbox_checked)
                checkboxAlphabet.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_checkbox_unchecked)
            } else {
                buttonView.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_checkbox_unchecked)
            }
        }
        when (viewModel.getSelectedSorting().value) {
            SortingVariants.ALPHABET -> {
                checkboxAlphabet.isChecked = true
                checkboxUser.isChecked = false
            }

            SortingVariants.USER -> {
                checkboxUser.isChecked = true
                checkboxAlphabet.isChecked = false
            }

            null -> {}
        }

        popupView.findViewById<LinearLayout>(R.id.alphabetSorting).setOnClickListener {
            binding.sortLayout.typeSort.text = SortingVariants.ALPHABET.toString()
            viewModel.setSorting(SortingVariants.ALPHABET)
            checkboxAlphabet.isChecked = true
            checkboxUser.isChecked = false
            popupWindow.dismiss()
        }

        popupView.findViewById<LinearLayout>(R.id.userSorting).setOnClickListener {
            binding.sortLayout.typeSort.text = SortingVariants.USER.toString()
            viewModel.setSorting(SortingVariants.USER)
            checkboxUser.isChecked = true
            checkboxAlphabet.isChecked = false
            popupWindow.dismiss()
        }
        popupWindow.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupWidth = popupWindow.contentView.measuredWidth

        val x = (anchor.width - popupWidth) * COEFFICIENT
        val y = -anchor.height
        popupWindow.showAsDropDown(anchor, x.toInt(), y)
    }

    private fun createProduct(shoppingListId: Int, view: View) {
        if (!nameProduct.isNullOrEmpty()) {
            viewModel.addProduct(
                ProductListItem(
                    shoppingListId = shoppingListId,
                    name = nameProduct!!,
                    position = productsAdapter.itemCount,
                    amount = amountProduct,
                    unit = unitProduct,
                )
            )
            hideKeyboard(view)
            bottomSheetAddProduct?.state = BottomSheetBehavior.STATE_HIDDEN
            clearFieldsProduct()
        } else {
            // думаю что тут можно сделать для валидации ввода, пока так
            Toast.makeText(
                requireContext(),
                "НЕОБХОДИМО ВВЕСТИ НАЗВАНИЕ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun swapImageFab(state: Int) {
        when (state) {
            BottomSheetBehavior.STATE_HIDDEN -> {
                binding.fabAddProduct.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_add
                    )
                )
            }

            BottomSheetBehavior.STATE_COLLAPSED -> {
                binding.fabAddProduct.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_confirm
                    )
                )
            }
        }
    }

    private fun clearFieldsProduct() {
        nameProduct = null
        unitProduct = null
        amountProduct = null
        with(binding) {
            editTextNameProduct.text?.clear()
            editTextNameProduct.clearFocus()
            editTextAmountProduct.text?.clear()
            editTextAmountProduct.clearFocus()
            completeTextUnit.text = null
            completeTextUnit.clearFocus()
        }
    }

    private fun initBottomSheet() {
        bottomSheetAddProduct = binding.bottomSheetAddProduct.let { BottomSheetBehavior.from(it) }
        bottomSheetAddProduct?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetMenu = binding.bottomSheetMenu.let { BottomSheetBehavior.from(it) }
        bottomSheetMenu?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showContent(productList: List<ProductListItem>) {
        with(binding) {
            placeholderProductsEmpty.isVisible = false
            rvProducts.isVisible = true
        }
        productsAdapter.setProductList(productList)
    }

    private fun showEmptyPlaceHolder() {
        with(binding) {
            placeholderProductsEmpty.isVisible = true
            rvProducts.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        productsAdapter.clearProductList()
        _binding = null
    }

    private fun showOverlay(show: Boolean) {
        binding.overlay.isVisible = show
        Animates.animateOverlay(show, binding.overlay)
    }

    private fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val COEFFICIENT = 4 / 5f
        const val KEY_PRODUCT_LIST = "KEY PRODUCT"
        fun createArgs(shoppingList: ShoppingListItem): Bundle =
            bundleOf(KEY_PRODUCT_LIST to shoppingList)
    }
}