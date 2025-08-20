package fr.qg.items.common.registries

import fr.qg.items.common.impl.EconomyImpl
import org.bukkit.plugin.java.JavaPlugin

object EconomyRegistry {

    enum class EconomyType(val plugin: String, val implClass: String) {
        SHOPGUIPLUS("ShopGuiPlus", "fr.qg.items.common.impl.eco.ShopGuiEconomyImpl"),
        NONE("", "fr.qg.items.common.impl.eco.NoEconomyImpl")
    }

    lateinit var eco: EconomyImpl

    fun resolveVersion(plugin: JavaPlugin) : EconomyType =
        EconomyType.entries.firstOrNull { plugin.server.pluginManager.getPlugin(it.plugin) != null }
            ?: EconomyType.NONE

    fun load(plugin: JavaPlugin)  {
        val implClass = Class.forName(resolveVersion(plugin).implClass)
        val ctor = implClass.getDeclaredConstructor()
        ctor.isAccessible = true
        eco = ctor.newInstance() as EconomyImpl
    }
}