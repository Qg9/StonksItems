package fr.qg.items.v21

import fr.qg.items.common.action.handler.HarvestActionHandler
import fr.qg.items.common.impl.VersionImpl
import fr.qg.items.common.models.ConfigurationItem
import fr.qg.items.v21.action.HarvestActionHandler21
import io.papermc.paper.datacomponent.item.attribute.AttributeModifierDisplay.override
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class VersionImpl21 : VersionImpl {
    override fun harvestHandler(): HarvestActionHandler = HarvestActionHandler21

    override fun buildItem(item: ConfigurationItem): ItemStack {
        val stack = ItemStack(item.material, 1)

        val meta = stack.itemMeta

        meta.displayName(item.name?.replace("&","§")?.let { Component.text(it) })
        meta.lore(
            item.lore.map {
                Component.text(it.replace("&","§"))
            }
        )

        meta.setCustomModelData(item.modelData)

        meta.addItemFlags(*item.itemFlags.toTypedArray())

        meta.isUnbreakable = item.unbreakable
        for (entry in item.enchantments)
            meta.addEnchant(entry.key, entry.value, true)

        stack.itemMeta = meta
        return stack
    }

    override fun itemInMainHand(player: Player): ItemStack? = player.itemInHand

}