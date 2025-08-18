package fr.qg.items.v8

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.common.VersionImplementation
import fr.qg.items.common.action.handler.HarvestActionHandler
import fr.qg.items.common.models.ConfigurationItem
import fr.qg.items.v8.action.HarvestActionHandler8
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack

class VersionImplementation8 : VersionImplementation {
    override fun harvestHandler(): HarvestActionHandler = HarvestActionHandler8

    override fun buildItem(item: ConfigurationItem): ItemStack {
        val stack = ItemStack(item.material, 1)

        val meta = stack.itemMeta

        item.name?.let { meta.displayName = ChatColor.translateAlternateColorCodes('&', it) }
        if (item.lore.isNotEmpty()) meta.lore = item.lore.map { ChatColor.translateAlternateColorCodes('&', it) }
        if (item.itemFlags.isNotEmpty()) meta.addItemFlags(*item.itemFlags.toTypedArray())
        stack.itemMeta = meta

        if (item.enchantments.isNotEmpty())
            item.enchantments.forEach { (ench, lvl) -> stack.addUnsafeEnchantment(ench, lvl) }

        item.customDurability?.let { stack.durability = it.toShort() }
        NBT.modify(stack) {
            if (item.unbreakable) it.setBoolean("Unbreakable", true)
        }

        return stack
    }
}
