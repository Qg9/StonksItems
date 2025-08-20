package fr.qg.items.commands

import com.jonahseguin.drink.annotation.*
import de.tr7zw.changeme.nbtapi.NBTItem
import fr.qg.items.ItemsPlugin
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.models.ConfigurationItem
import fr.qg.items.managers.ItemsManager
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ItemsCommand {

    @Require("items.give")
    @Command(name = "give", desc = "Give un item configuré", usage = "/items give <item> <params..>")
    fun give(@Sender player: Player, item: ConfigurationItem, @OptArg("") @Text args: String) {

        val tokens = parseArguments(args)

        val stack = ItemsManager.createItem(item, tokens)//.apply { this.amount = amount.coerceAtLeast(1) }
        val leftover = player.inventory.addItem(stack)

        if (leftover.isNotEmpty()) {
            leftover.values.forEach { player.world.dropItemNaturally(player.location, it) }
            player.sendMessage("${ChatColor.YELLOW}Inventaire plein, l'item a été drop.")
        } else {
            val name = stack.itemMeta?.displayName ?: stack.type.name
            player.sendMessage("${ChatColor.GREEN}Tu as reçu: ${ChatColor.WHITE}$name x${stack.amount}")
        }
    }

    @Require("items.info")
    @Command(name = "info", desc = "Info sur l'item", usage = "/items info")
    fun info(@Sender player: Player) {
        val stack = player.itemInHand ?: return
        player.sendMessage(NBTItem(stack).toString())
    }

    @Require("items.set")
    @Command(name = "set", desc = "Set l'item", usage = "/items set <player> <item> <params..>")
    fun set(@Sender player: Player, item: ConfigurationItem, @OptArg("") @Text args: String) {

        val tokens = parseArguments(args)

        val stack = ItemsManager.createItem(item, tokens)//.apply { this.amount = amount.coerceAtLeast(1) }
        player.inventory.itemInHand = stack
    }

    @Require("items.reload")
    @Command(name = "reload", desc = "Reload le plugin", usage = "/items reload")
    fun reload(@Sender player: Player) {
        ItemsPlugin.plugin.reloadConfig()
        ItemsManager.load()
    }

    fun parseArguments(input: String): Map<String, Pair<String, ItemProperty.TypeStorage>> {
        val tokens = input.split(" ")
        val result = mutableMapOf<String, Pair<String, ItemProperty.TypeStorage>>()
        var prefix = ""

        for (token in tokens) {
            when {
                token == ":i" -> prefix = ItemProperty.PREFIX_ITEMS
                token == ":s" -> prefix = StatisticsAction.STATS_PREFIX
                token == ":nbt" -> prefix = ""
                token.contains("=") -> {
                    val (key, rawValue) = token.split("=", limit = 2)

                    val (value, typeStr) = if ('$' in rawValue) {
                        rawValue.split("$", limit = 2)
                    } else {
                        listOf(rawValue, "STRING") // fallback si pas de type
                    }

                    val type = try {
                        ItemProperty.TypeStorage.valueOf(typeStr.uppercase())
                    } catch (_: Exception) {
                        ItemProperty.TypeStorage.STRING
                    }

                    result["$prefix$key"] = value to type
                }
            }
        }
        return result
    }
}
