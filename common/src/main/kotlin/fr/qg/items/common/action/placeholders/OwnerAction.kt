package fr.qg.items.common.action.placeholders

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.ItemProperty

object OwnerAction : PlaceholdersItemPropertyAction {

    override fun generate(nbt: ReadableItemNBT, placeholders: MutableMap<String, String>) {
        if (!ItemProperty.OWNER.has(nbt)) return

        placeholders[ItemProperty.OWNER.placeholderkey] = ItemProperty.OWNER.get(nbt)
    }
}