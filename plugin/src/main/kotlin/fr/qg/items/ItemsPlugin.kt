package fr.qg.items

import com.jonahseguin.drink.command.DrinkCommandService
import fr.qg.items.commands.ConfigurationItemProvider
import fr.qg.items.commands.ItemsCommand
import fr.qg.items.common.VersionImplementation
import fr.qg.items.common.models.ConfigurationItem
import fr.qg.items.managers.ItemsManager
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.zapper.ZapperJavaPlugin

class ItemsPlugin : ZapperJavaPlugin() {

    companion object {
        lateinit var plugin: JavaPlugin
        lateinit var implementation: VersionImplementation
    }

    override fun onEnable() {

        saveDefaultConfig()

        plugin = this
        implementation = VersionLoader.load()

        ItemsManager.load()

        ItemsManager.actions.values.filter { it is Listener }.forEach { server.pluginManager.registerEvents(it as Listener, this) }

        val drink = DrinkCommandService(this)
        drink.bind(ConfigurationItem::class.java).toProvider(ConfigurationItemProvider)
        drink.register(ItemsCommand, "qgitems", "caraibemcitems", "items", "i")
        drink.registerCommands()
    }
}