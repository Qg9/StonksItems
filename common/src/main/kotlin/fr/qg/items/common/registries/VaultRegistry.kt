package fr.qg.items.common.registries

import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

object VaultRegistry {

    lateinit var economy: Economy

    fun setupEconomy(plugin: JavaPlugin) {
        if (plugin.server.pluginManager.getPlugin("Vault") == null) error("plugin require vault to work 1")
        val rsp: RegisteredServiceProvider<Economy?>? =
            plugin.server.servicesManager.getRegistration(Economy::class.java)
        if (rsp == null) error("plugin require vault to work 2 ")
        economy = rsp.getProvider() ?: error("plugin require vault to work 3")
        println("Loaded Vault Hook !")
    }
}