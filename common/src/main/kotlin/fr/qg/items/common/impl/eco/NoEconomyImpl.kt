package fr.qg.items.common.impl.eco

import fr.qg.items.common.impl.EconomyImpl
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object NoEconomyImpl : EconomyImpl {
    override fun price(player: Player, type: Material): Double = 0.0

    override fun price(player: Player, stack: ItemStack): Double = 0.0
}