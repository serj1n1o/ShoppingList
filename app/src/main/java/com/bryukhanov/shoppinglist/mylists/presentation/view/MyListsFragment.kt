package com.bryukhanov.shoppinglist.mylists.presentation.view

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        binding.rvMyLists.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyListsFragment.adapter
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                adapter.showActions(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val buttonContainerWidth = itemView.findViewById<View>(R.id.buttonContainer).width
                val maxDx = -buttonContainerWidth.toFloat() // Ограничиваем свайп до ширины кнопок

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX.coerceAtLeast(maxDx), // Свайп ограничен до кнопок
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvMyLists)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

