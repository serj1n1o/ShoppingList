package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryukhanov.shoppinglist.R
import com.bryukhanov.shoppinglist.databinding.FragmentMyListsBinding
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.mylists.presentation.adapters.ShoppingListAdapter


class MyListsFragment : Fragment() {
    private var _binding: FragmentMyListsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShoppingListAdapter

    private val fakeData = mutableListOf(
        ShoppingListItem(id = 1, name = "Продукты", cover = R.drawable.ic_list),
        ShoppingListItem(id = 2, name = "Для дома", cover = R.drawable.ic_list),
        ShoppingListItem(id = 3, name = "Подарки к Новому году", cover = R.drawable.ic_list)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShoppingListAdapter(fakeData, object : ShoppingListAdapter.ActionListener {
            override fun onEdit(position: Int) {
                val item = fakeData[position]
                Toast.makeText(context, "Редактирование: ${item.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onCopy(position: Int) {
                val item = fakeData[position]
                Toast.makeText(context, "Копирование: ${item.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onDelete(position: Int) {
                fakeData.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
            }
        })

        adapter.notifyDataSetChanged()

        binding.rvMyLists.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyListsFragment.adapter
        }

        val itemTouchHelper = ItemTouchHelper(SwipeCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.rvMyLists)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


