package fr.qg.items.v21.action

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.action.handler.HarvestActionHandler
import fr.qg.items.common.models.WriteableNBT
import fr.qg.items.common.registries.JobsRegistry
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import org.bukkit.entity.Player

object HarvestActionHandler21 : HarvestActionHandler{
    override fun handle(
        player: Player,
        nbt: ReadableItemNBT,
        writer: WriteableNBT,
        block: Block,
        radius: Int,
        fortune: Int
    ): Map<Material, Int> {

        val world = block.world
        val collected = mutableMapOf<Material, Int>()
        val item = player.activeItem

        for (x in -radius..radius) {
            for (z in -radius..radius) {

                val target = world.getBlockAt(
                    block.location.clone().add(x.toDouble(), 0.0, z.toDouble())
                )

                val ageable = target.blockData as? Ageable ?: continue

                if (ageable.age != ageable.maximumAge) continue

                target.getDrops(item).map {
                    it.type to it.amount
                }.forEach {
                    collected.merge(it.first, it.second*fortune, Int::plus)
                }
                JobsRegistry.jobs.actions(player, block)
                ageable.age = 0
                target.blockData = ageable
            }
        }
        return collected
    }
}