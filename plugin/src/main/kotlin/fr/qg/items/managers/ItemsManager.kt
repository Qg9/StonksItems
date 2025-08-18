package fr.qg.items.managers

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.ItemsPlugin
import fr.qg.items.action.HarvestAction
import fr.qg.items.action.InfiniteAction
import fr.qg.items.action.OtherStatisticsAction
import fr.qg.items.action.ShiftClickAction
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.LoreChangeAction
import fr.qg.items.common.action.NameChangeAction
import fr.qg.items.common.action.placeholders.OwnerAction
import fr.qg.items.common.action.placeholders.PlaceholdersItemPropertyAction
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.action.placeholders.UseAction
import fr.qg.items.common.managers.ConfigManager
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.ConfigurationItem
import org.bukkit.inventory.ItemStack

object ItemsManager {

    val items: MutableMap<String, ConfigurationItem> = mutableMapOf()

    val actions = mapOf(
        ItemProperty.HARVEST_RADIUS to listOf(HarvestAction),
        ItemProperty.USE to listOf(UseAction),
        ItemProperty.LORE_CHANGE to listOf(LoreChangeAction),
        ItemProperty.NAME_CHANGE to listOf(NameChangeAction),
        ItemProperty.OWNER to listOf(OwnerAction),
        ItemProperty.STATISTICS to listOf(StatisticsAction, OtherStatisticsAction),
        ItemProperty.INFINITE to listOf(InfiniteAction),
        ItemProperty.SHIFT_CLICK_ACTION to listOf(ShiftClickAction)
    )

    fun load() {
        items.clear()
        items.putAll(ConfigManager.load(ItemsPlugin.plugin))
    }

    fun createItem(item: ConfigurationItem, defaults: Map<String, Pair<String, ItemProperty.TypeStorage>>) : ItemStack {
        val stack = ItemsPlugin.implementation.buildItem(item)

        val placeholders = mutableMapOf<String, String>()

        NBT.modify(stack) {
            if (item.properties.isNotEmpty())
                item.properties.forEach { (prop: ItemProperty, value: Any) -> prop.set(it, value) }

            defaults.forEach { key, (value, type) ->
                type.setToItem(it, key, type.parse(value))
            }
        }

        NBT.get(stack) {
            item.properties.keys.filter { actions[it] is PlaceholdersItemPropertyAction }.forEach { t ->
                (actions[t] as PlaceholdersItemPropertyAction).generate(it, placeholders)
            }

            println(placeholders)

            ItemActionManager.updateItem(it, stack, placeholders)
        }

        return stack
    }
}