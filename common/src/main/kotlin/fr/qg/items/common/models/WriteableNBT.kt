package fr.qg.items.common.models

import de.tr7zw.changeme.nbtapi.NBT
import de.tr7zw.changeme.nbtapi.iface.ReadWriteItemNBT
import org.bukkit.inventory.ItemStack

class WriteableNBT(val actions: MutableList<(ReadWriteItemNBT) -> Unit> = mutableListOf()) {

    fun write(action: (ReadWriteItemNBT) -> Unit) = actions.add(action)

    fun apply(item: ItemStack) {
        NBT.modify(item) { nbt ->
            actions.forEach { it.invoke(nbt) }
        }
    }
}