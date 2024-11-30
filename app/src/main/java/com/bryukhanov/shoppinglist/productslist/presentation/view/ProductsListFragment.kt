package com.bryukhanov.shoppinglist.productslist.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bryukhanov.shoppinglist.R

class ProductsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_products_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ProductsListFragment()
    }
}