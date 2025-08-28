package fr.qg.items.common.action

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.ItemProperty
import org.bukkit.inventory.meta.ItemMeta

object LoreChangeAction : ItemPropertyAction {

    fun apply(nbt: ReadableItemNBT, meta: ItemMeta, placeholders: Set<Map.Entry<String, String>>) {
        if (!ItemProperty.LORE_CHANGE.has(nbt)) return

        val lore = ItemProperty.LORE_CHANGE.get<List<String>>(nbt)
        meta.setLore(lore.map { placeholders.operate(it) { s, k, v -> s.replace("%$k%", v) }.replace("&","§") })
    }

    fun applyToCurrentLore(meta: ItemMeta, placeholders: Set<Map.Entry<String, String>>) {

        val lore = meta.lore
        meta.setLore(lore.map { placeholders.operate(it) { s, k, v -> s.replace("%$k%", v) }.replace("&","§") })
    }
}

