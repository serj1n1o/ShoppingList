package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                ShoppingListItem(
                    id = 2,
                    name = "Для дома хотелось бы что нибудь купить",
                    cover = R.drawable.ic_list
                ),
                ShoppingListItem(
                    id = 3,
                    name = "Подарки к Новому году и кое что еще, а может сразу и на старый новый год купим",
                    cover = R.drawable.ic_list
                )
            )
        )

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