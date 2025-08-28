package fr.qg.items.common.action

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.ItemProperty
import org.bukkit.inventory.meta.ItemMeta

object NameChangeAction : ItemPropertyAction {

    fun apply(nbt: ReadableItemNBT, meta: ItemMeta, placeholders: Set<Map.Entry<String, String>>) {
        if (!ItemProperty.NAME_CHANGE.has(nbt)) return

        val name = ItemProperty.NAME_CHANGE.get<String>(nbt)
        meta.setDisplayName(placeholders.operate(name) { s, k, v -> s.replace("%$k%", v) }.replace("&","§"))
    }

    fun applyToCurrentLore(meta: ItemMeta, placeholders: Set<Map.Entry<String, String>>) {

        val name = meta.displayName
        meta.setDisplayName(placeholders.operate(name) { s, k, v -> s.replace("%$k%", v) }.replace("&","§"))
    }
}

fun <K, V, H> Set<Map.Entry<K, V>>.operate(def: H, operator: (H, K, V) -> H) : H {
    var change = def

    for ((k, v) in this)
        change = operator(change, k, v)

    return change
}