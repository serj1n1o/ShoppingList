package com.bryukhanov.shoppinglist.mylists.data

import com.bryukhanov.shoppinglist.core.util.NameShoppingListGenerator
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toDbo
import com.bryukhanov.shoppinglist.db.converters.ShoppingListConverter.toUiShoppingList
import com.bryukhanov.shoppinglist.mylists.data.db.dao.ShoppingListDao
import com.bryukhanov.shoppinglist.mylists.data.db.entity.ShoppingListItemDbo
import com.bryukhanov.shoppinglist.mylists.domain.api.ShoppingListRepository
import com.bryukhanov.shoppinglist.mylists.domain.models.ShoppingListItem
import com.bryukhanov.shoppinglist.productslist.domain.api.UseCaseProductsFromShoppingList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ShoppingListRepositoryImpl(
    private val shoppingListDao: ShoppingListDao,
    private val nameShoppingListGenerator: NameShoppingListGenerator,
    private val useCaseProductsFromShoppingList: UseCaseProductsFromShoppingList,

    ) : ShoppingListRepository {

    override fun getAllShoppingLists(): Flow<Result<List<ShoppingListItem>>> {
        return shoppingListDao.getAllShoppingList()
            .map { listDbo ->
                try {
                    Result.success(listDbo.toUiShoppingList())
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun addShoppingList(shoppingListItem: ShoppingListItem): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                shoppingListDao.addShoppingList(shoppingListItem.toDbo())
            }
        }
    }

    override suspend fun updateShoppingList(shoppingListItem: ShoppingListItem): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                shoppingListDao.updateShoppingList(shoppingListItem.toDbo())
            }
        }
    }

    override suspend fun deleteShoppingList(shoppingListItem: ShoppingListItem): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                shoppingListDao.deleteShoppingListItem(shoppingListItem.toDbo())
            }
        }
    }

    override suspend fun deleteAllLists(): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                shoppingListDao.deleteAllShoppingList()
            }
        }
    }

    override suspend fun copyShoppingList(shoppingListId: Int): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val originalList = shoppingListDao.getShoppingListById(shoppingListId)
                    ?: throw IllegalStateException()
                val products =
                    useCaseProductsFromShoppingList.getProductsFromShoppingList(shoppingListId)
                        .firstOrNull()
                        ?.getOrElse { throw it }

                val existingNames = shoppingListDao.getAllShoppingList().map { listDbo ->
                    listDbo.map { it.name }
                }.first()

                val newName = nameShoppingListGenerator.generateUniqueName(
                    baseName = originalList.name,
                    existingNames = existingNames
                )

                val newShoppingList =
                    ShoppingListItemDbo(
                        name = newName,
                        cover = originalList.cover,
                        sortType = originalList.sortType
                    )
                val newShoppingListId =
                    shoppingListDao.addShoppingList(newShoppingList).toInt()

                val newProducts =
                    products?.map { it.copy(id = 0, shoppingListId = newShoppingListId) }
                if (newProducts != null) {
                    useCaseProductsFromShoppingList.addProducts(newProducts)
                }
            }
        }
    }
}