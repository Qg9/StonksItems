package fr.qg.items.common.impl

import fr.qg.items.common.action.handler.HarvestActionHandler
import fr.qg.items.common.models.ConfigurationItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface VersionImpl {

    fun harvestHandler() : HarvestActionHandler

    fun buildItem(item: ConfigurationItem) : ItemStack

    fun itemInMainHand(player: Player) : ItemStack?

    fun logs() : List<Material>
}