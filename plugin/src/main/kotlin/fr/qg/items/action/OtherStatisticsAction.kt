package fr.qg.items.action

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.ItemPropertyAction
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.WriteableNBT
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

object OtherStatisticsAction : Listener, ItemPropertyAction {

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        val item = player.itemInHand ?: return
        val type = e.block.type.name.lowercase()
        applyStatistics(item) { mapOf("block_$type" to 1) }
    }

    @EventHandler
    fun onEntityKill(e: EntityDeathEvent) {
        val killer = e.entity.killer ?: return
        val item = killer.itemInHand ?: return
        val type = e.entity.type.name.lowercase()
        applyStatistics(item) { mapOf("kill_$type" to 1) }
    }

    private fun applyStatistics(item: ItemStack, statsGetter: () -> Map<String, Int>) {
        NBT.get(item) { nbt ->
            if (!ItemProperty.STATISTICS.has(nbt)) return@get
            val writer = WriteableNBT()
            val placeholders = mutableMapOf<String, String>()
            StatisticsAction.apply(nbt, writer, placeholders, statsGetter)
            writer.apply(item)
            ItemActionManager.updateItem(nbt, item, placeholders)
        }
    }
}
