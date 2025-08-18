package fr.qg.items.common.managers

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.LoreChangeAction
import fr.qg.items.common.action.NameChangeAction
import org.bukkit.inventory.ItemStack

object ItemActionManager {

    fun updateItem(nbt: ReadableItemNBT, stack: ItemStack, placeholders: Map<String, String>) {
        if (ItemProperty.NAME_CHANGE.has(nbt) || ItemProperty.LORE_CHANGE.has(nbt)) {
            val meta =stack.itemMeta
            val changes = placeholders.entries
            NameChangeAction.apply(nbt, meta, changes)
            LoreChangeAction.apply(nbt, meta, changes)
            stack.itemMeta = meta
        }
    }
}