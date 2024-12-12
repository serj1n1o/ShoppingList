package com.bryukhanov.shoppinglist.core.util

enum class Units(private val unitName: String) {
    PIECE("шт"),
    PACKAGE("уп"),
    KILOGRAM("кг"),
    GRAM("г"),
    LITER("л"),
    MILLILITER("мл"),
    PACK("пач");

    override fun toString(): String = unitName
}