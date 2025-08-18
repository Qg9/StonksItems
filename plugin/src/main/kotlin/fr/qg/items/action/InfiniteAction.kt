package fr.qg.items.action

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.ItemsPlugin
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.ItemPropertyAction
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

object InfiniteAction : Listener, ItemPropertyAction {

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        val player = e.entity
        val keep = mutableListOf<ItemStack>()
        e.drops.forEach { item ->
            if (isInfinite(item)) keep += item
        }
        if (keep.isNotEmpty()) {
            e.drops.removeAll(keep.toSet())
            giveBackLater(player, keep)
        }
    }

    private fun isInfinite(item: ItemStack?): Boolean {
        if (item == null) return false
        return NBT.get<Boolean>(item) { nbt -> ItemProperty.INFINITE.has(nbt) }
    }

    private fun giveBackLater(player: Player, items: List<ItemStack>) {
        player.server.scheduler.runTask(ItemsPlugin.plugin) {
            items.forEach { player.inventory.addItem(it) }
        }
    }
}