package com.bryukhanov.shoppinglist.core.util

enum class SortingVariants(private val nameSort: String) {
    ALPHABET("По алфавиту"),
    USER("Пользовательская");

    override fun toString(): String = nameSort
}