package com.bryukhanov.shoppinglist.core.util

object NameShoppingListGenerator {

    fun generateUniqueName(baseName: String, existingNames: List<String>): String {
        var newName = "$baseName (копия)"
        var counter = 1
        while (existingNames.contains(newName)) {
            newName = "$baseName (копия $counter)"
            counter++
        }
        return newName
    }
}