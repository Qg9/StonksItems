package fr.qg.items.action

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.ItemPropertyAction
import fr.qg.items.common.action.placeholders.OwnerAction
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.action.placeholders.UseAction
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.WriteableNBT
import fr.qg.items.common.registries.EconomyRegistry
import fr.qg.items.common.registries.VaultRegistry
import fr.qg.items.common.registries.VersionRegistry
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

object TreeChopAction : Listener, ItemPropertyAction {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val item = VersionRegistry.version.itemInMainHand(player) ?: return
        if (item.type == Material.AIR) return

        val block = event.block
        if (block.type == Material.AIR) return

        NBT.get(item) { nbt ->
            if (!ItemProperty.TREE_CHOPPER.has(nbt)) return@get
            if (!VersionRegistry.version.logs().contains(block.type)) return@get

            event.isCancelled = true
            val writer = WriteableNBT()

            val collected = chopTree(block)

            val autosell = if (ItemProperty.AUTOSELL.has(nbt))
                ItemProperty.AUTOSELL.get(nbt)
            else false

            if (!autosell) {
                for ((material, totalAmount) in collected) {
                    val maxStack = material.maxStackSize.coerceAtLeast(1)
                    var remaining = totalAmount
                    while (remaining > 0) {
                        val stackSize = remaining.coerceAtMost(maxStack)
                        player.inventory.addItem(ItemStack(material, stackSize))
                        remaining -= stackSize
                    }
                }
            } else {
                var price = 0.0
                for ((material, totalAmount) in collected) {
                    price += EconomyRegistry.eco.price(player, material) * totalAmount
                }
                VaultRegistry.economy.depositPlayer(player, price)
            }

            val placeholders = mutableMapOf<String, String>()
            if (!UseAction.use(nbt, writer, 1, placeholders)) {
                item.type = Material.AIR
                player.playSound(player.location, Sound.ITEM_BREAK, 1f, 1f)
                return@get
            }

            StatisticsAction.apply(nbt, writer, placeholders) {
                collected.mapKeys { it.key.name.lowercase() }
            }
            OwnerAction.generate(nbt, placeholders)

            writer.apply(player.itemInHand)
            ItemActionManager.updateItem(nbt, item, placeholders)
        }
    }

    private fun chopTree(start: Block): Map<Material, Int> {
        val visited = mutableSetOf<Block>()
        val drops = mutableMapOf<Material, Int>()

        fun dfs(block: Block) {
            if (!VersionRegistry.version.logs().contains(block.type)) return
            if (!visited.add(block)) return

            block.type = Material.AIR
            drops[block.type] = drops.getOrDefault(block.type, 0) + 1

            for (x in -1..1) for (y in -1..1) for (z in -1..1) {
                val relative = block.world.getBlockAt(
                    block.x + x, block.y + y, block.z + z
                )
                dfs(relative)
            }
        }

        dfs(start)
        return drops
    }
}