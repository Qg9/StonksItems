package fr.qg.items.action

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.ItemPropertyAction
import fr.qg.items.common.action.placeholders.OwnerAction
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.action.placeholders.UseAction
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.WriteableNBT
import fr.qg.items.common.registries.VersionRegistry
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

object HammerAction : Listener, ItemPropertyAction {

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {

        val block = event.block
        val player = event.player
        val item = VersionRegistry.version.itemInMainHand(player) ?: return

        NBT.get(item) {
            if (!ItemProperty.HAMMER.has(it)) return@get

            val radius = ItemProperty.HAMMER.get<Int>(it)
            val types = mutableMapOf<Material, Int>()

            for (x in -radius..radius)
                for (y in -radius..radius)
                    for (z in -radius..radius) {
                        val target = block.location.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block
                        if(target.type == Material.AIR)continue
                        types.merge(target.type, 1, Int::plus)
                        target.breakNaturally(item)
                    }

            val writer = WriteableNBT()

            val placeholders = mutableMapOf<String, String>()
            if(!UseAction.use(it, writer, 1, placeholders)) {
                player.itemInHand = null
                player.playSound(player.location, Sound.ITEM_BREAK, 1f, 1f)
                return@get
            }

            StatisticsAction.apply(it, writer, placeholders) {
                types.mapKeys { it.key.name.lowercase() }
            }
            OwnerAction.generate(it, placeholders)

            writer.apply(player.itemInHand)
            ItemActionManager.updateItem(it, item, placeholders)
        }
    }
}