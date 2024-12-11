package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.FragmentMyListsBinding
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.mylists.presentation.adapters.ShoppingListAdapter
import com.bryukhanov.shoppinglist.productslist.presentation.view.ProductsListFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


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
        val list = listOf(
            ShoppingListItem(id = 1, name = "Продукты", cover = R.drawable.ic_list),
            ShoppingListItem(id = 2, name = "Для дома", cover = R.drawable.ic_list),
            ShoppingListItem(id = 3, name = "Подарки к Новому году", cover = R.drawable.ic_list)
        )

        val repository by inject<ShoppingListRepository>()

        viewLifecycleOwner.lifecycleScope.launch {
            repository.addShoppingList(
                ShoppingListItem(
                    id = 3,
                    name = "Подарки к Новому году",
                    cover = R.drawable.ic_list
                )
            )
        }


        // Инициализация адаптера. Фейковые данные для тестирования
        adapter = ShoppingListAdapter(list, object : ShoppingListAdapter.ClickListener {
            override val onClick: (id: Int) -> Unit
                get() = { id ->
                    findNavController().navigate(
                        R.id.action_myListsFragment_to_productsListFragment,
                        ProductsListFragment.createArgs(id)
                    )
                }

        })

        binding.rvMyLists.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyListsFragment.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}