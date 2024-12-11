package com.bryukhanov.shoppinglist.productslist.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.core.util.Animates
import com.bryukhanov.shoppinglist.databinding.FragmentProductsListBinding
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

        val shoppingListId = requireArguments().getInt(KEY_PRODUCT_LIST, 0)

        viewModel.getProducts(shoppingListId)

        binding.rvProducts.adapter = productsAdapter

        viewModel.getProductState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProductsState.Content -> showContent(state.productList)
                ProductsState.Empty -> showEmptyPlaceHolder()
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
                        binding.fabAddProduct.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_confirm
                            )
                        )
                        binding.overlay.isVisible = true
                        Animates.animateOverlay(true, binding.overlay)
                        binding.fabAddProduct.setOnClickListener {
                            viewModel.addProduct(
                                ProductListItem(
                                    shoppingListId = shoppingListId,
                                    name = "Test name",
                                    position = 1,
                                    amount = null,
                                    unit = null
                                )
                            )
                            bottomSheetAddProduct?.state = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                        binding.fabAddProduct.setOnClickListener {
                            bottomSheetAddProduct?.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                        binding.fabAddProduct.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_add
                            )
                        )
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })

        binding.editTextNameProduct.doOnTextChanged { text, start, before, count ->

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

    companion object {
        const val KEY_PRODUCT_LIST = "KEY PRODUCT"
        fun createArgs(id: Int): Bundle = bundleOf(KEY_PRODUCT_LIST to id)
    }
}