package fr.qg.items.v8.action

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.action.handler.HarvestActionHandler
import fr.qg.items.common.action.placeholders.OwnerAction
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.action.placeholders.UseAction
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.WriteableNBT
import fr.qg.items.v8.harvest.HarvestType
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ThreadLocalRandom

object HarvestActionHandler8 : HarvestActionHandler {

    override fun handle(player: Player, nbt: ReadableItemNBT, writer: WriteableNBT, block: Block, radius: Int, fortune: Int, autosell: Boolean) {

        val world = block.world
        val originType = block.type
        val harvestType = HarvestType.Companion.fromBlock(originType) ?: return
        val collected = mutableMapOf<Material, Int>()

        for (x in -radius..radius) {
            for (z in -radius..radius) {
                val target = world.getBlockAt(
                    block.location.clone().add(x.toDouble(), 0.0, z.toDouble())
                )

                if (!harvestType.isMature.test(target)) continue

                val amount = ThreadLocalRandom.current().nextInt(
                    harvestType.minAmount,
                    harvestType.maxAmount + 1
                )*fortune

                collected.merge(harvestType.dropType, amount, Int::plus)
                harvestType.reset.invoke(target)
            }
        }

        val placeholders = mutableMapOf<String, String>()
        UseAction.use(nbt, writer, 1, placeholders)
        StatisticsAction.apply(nbt, writer, placeholders) {
            collected.mapKeys { it.key.name.lowercase() }
        }
        OwnerAction.generate(nbt, placeholders)

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
            // TODO À implémenter : vente automatique via économie
        }

        writer.apply(player.itemInHand)

        ItemActionManager.updateItem(nbt, player.itemInHand, placeholders)
    }
}
