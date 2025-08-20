package fr.qg.items.common.impl.eco

import fr.qg.items.common.impl.EconomyImpl
import net.brcdev.shopgui.ShopGuiPlusApi
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ShopGuiEconomyImpl : EconomyImpl {

    override fun price(player: Player, type: Material): Double =
       price(player, ItemStack(type))

    override fun price(player: Player, stack: ItemStack): Double =
        ShopGuiPlusApi.getItemStackPriceSell(player, stack)
}