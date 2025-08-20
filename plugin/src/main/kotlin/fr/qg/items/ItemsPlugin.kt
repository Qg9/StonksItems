package fr.qg.items

import com.jonahseguin.drink.command.DrinkCommandService
import fr.qg.items.commands.ConfigurationItemProvider
import fr.qg.items.commands.ItemsCommand
import fr.qg.items.common.impl.VersionImpl
import fr.qg.items.common.models.ConfigurationItem
import fr.qg.items.common.registries.EconomyRegistry
import fr.qg.items.common.registries.JobsRegistry
import fr.qg.items.common.registries.VaultRegistry
import fr.qg.items.common.registries.VersionRegistry
import fr.qg.items.managers.ItemsManager
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.zapper.ZapperJavaPlugin

class ItemsPlugin : ZapperJavaPlugin() {

    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {

        saveDefaultConfig()

        plugin = this
        VersionRegistry.load()
        EconomyRegistry.load(plugin)
        JobsRegistry.load(plugin)
        VaultRegistry.setupEconomy(plugin)

        ItemsManager.load()

        ItemsManager.actions.values.filter { it is Listener }.forEach {
            it.forEach { server.pluginManager.registerEvents(it as Listener, this)  }
        }

        val drink = DrinkCommandService(this)
        drink.bind(ConfigurationItem::class.java).toProvider(ConfigurationItemProvider)
        drink.register(ItemsCommand, "qgitems", "caraibemcitems", "items", "i")
        drink.registerCommands()
    }
}