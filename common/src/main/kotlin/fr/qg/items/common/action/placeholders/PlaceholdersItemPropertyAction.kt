package fr.qg.items.common.action.placeholders

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.action.ItemPropertyAction

interface PlaceholdersItemPropertyAction : ItemPropertyAction {

    fun generate(nbt: ReadableItemNBT, placeholders: MutableMap<String, String>)
}