package fr.qg.items.action

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.ItemsPlugin
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.ItemPropertyAction
import fr.qg.items.common.models.WriteableNBT
import fr.qg.items.common.registries.VersionRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

object HarvestAction : Listener, ItemPropertyAction {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {

        val player = event.player
        val item = player.itemInHand ?: return
        val block = event.block

        NBT.get(item) { nbt ->
            if (!ItemProperty.HARVEST_RADIUS.has(nbt)) return@get

            val radius = ItemProperty.HARVEST_RADIUS.get<Int>(nbt)
            val fortune = if (ItemProperty.HARVEST_FORTUNE.has(nbt))
                ItemProperty.HARVEST_FORTUNE.get(nbt)
            else 1

            val autosell = if (ItemProperty.HARVEST_AUTOSELL.has(nbt))
                ItemProperty.HARVEST_AUTOSELL.get(nbt)
            else false

            event.isCancelled = true

            val writer = WriteableNBT()

            VersionRegistry.version.harvestHandler().handle(
                player = player,
                nbt = nbt,
                writer = writer,
                block = block,
                radius = radius,
                fortune = fortune,
                autosell = autosell
            )
        }
    }
}
