package fr.qg.items.common.impl

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface EconomyImpl {

    fun price(player: Player, type: Material) : Double
    fun price(player: Player, stack: ItemStack) : Double

}