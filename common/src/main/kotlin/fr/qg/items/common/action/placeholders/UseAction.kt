package fr.qg.items.common.action.placeholders

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.models.WriteableNBT

object UseAction : PlaceholdersItemPropertyAction {

    fun use(nbt: ReadableItemNBT, writer: WriteableNBT, amount: Int, placeholders: MutableMap<String, String>): Boolean {
        if (!ItemProperty.USE.has(nbt)) return true

        val current = ItemProperty.USE.get<Int>(nbt)
        val newValue = current - amount
        ItemProperty.USE.set(writer, newValue)

        placeholders[ItemProperty.USE.placeholderkey] = newValue.toString()

        return newValue > 0
    }

    override fun generate(nbt: ReadableItemNBT, placeholders: MutableMap<String, String>) {
        use(nbt, WriteableNBT(), 0, placeholders)
    }
}