package fr.qg.items.v8.action

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.action.handler.HarvestActionHandler
import fr.qg.items.common.action.placeholders.OwnerAction
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.action.placeholders.UseAction
import fr.qg.items.common.impl.eco.ShopGuiEconomyImpl.price
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.WriteableNBT
import fr.qg.items.common.registries.EconomyRegistry
import fr.qg.items.common.registries.JobsRegistry
import fr.qg.items.common.registries.VaultRegistry
import fr.qg.items.v8.harvest.HarvestType
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ThreadLocalRandom

object HarvestActionHandler8 : HarvestActionHandler {

    override fun handle(player: Player, nbt: ReadableItemNBT, writer: WriteableNBT, block: Block, radius: Int, fortune: Int) : Map<Material, Int> {

        val world = block.world
        val collected = mutableMapOf<Material, Int>()

        for (x in -radius..radius) {
            for (z in -radius..radius) {

                val target = world.getBlockAt(
                    block.location.clone().add(x.toDouble(), 0.0, z.toDouble())
                )

                val harvestType = HarvestType.fromBlock(target.type) ?: continue

                if (!harvestType.isMature.test(target)) continue

                val amount = ThreadLocalRandom.current().nextInt(
                    harvestType.minAmount,
                    harvestType.maxAmount + 1
                )*fortune

                JobsRegistry.jobs.actions(player, block)
                collected.merge(harvestType.dropType, amount, Int::plus)
                harvestType.reset.invoke(target)
            }
        }

        return collected
    }
}
