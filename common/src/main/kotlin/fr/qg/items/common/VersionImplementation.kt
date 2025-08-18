package fr.qg.items.common

import fr.qg.items.common.action.handler.HarvestActionHandler
import fr.qg.items.common.models.ConfigurationItem
import org.bukkit.inventory.ItemStack

interface VersionImplementation {

    fun harvestHandler() : HarvestActionHandler

    fun buildItem(item: ConfigurationItem) : ItemStack
}