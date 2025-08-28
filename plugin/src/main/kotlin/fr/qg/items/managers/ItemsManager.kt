package fr.qg.items.managers

import de.tr7zw.changeme.nbtapi.NBT
import fr.qg.items.ItemsPlugin
import fr.qg.items.action.HammerAction
import fr.qg.items.action.HarvestAction
import fr.qg.items.action.InfiniteAction
import fr.qg.items.action.OtherStatisticsAction
import fr.qg.items.action.ShiftClickAction
import fr.qg.items.action.TreeChopAction
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.ItemPropertyAction
import fr.qg.items.common.action.LoreChangeAction
import fr.qg.items.common.action.NameChangeAction
import fr.qg.items.common.action.placeholders.OwnerAction
import fr.qg.items.common.action.placeholders.PlaceholdersItemPropertyAction
import fr.qg.items.common.action.placeholders.StatisticsAction
import fr.qg.items.common.action.placeholders.UseAction
import fr.qg.items.common.managers.ConfigManager
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.ConfigurationItem
import fr.qg.items.common.registries.VersionRegistry
import fr.qg.items.common.utils.linearise
import org.bukkit.craftbukkit.libs.jline.console.KeyMap.meta
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
        ItemProperty.SHIFT_CLICK_ACTION to listOf(ShiftClickAction),
        ItemProperty.HAMMER to listOf(HammerAction),
        ItemProperty.TREE_CHOPPER to listOf(TreeChopAction)
    )

    fun load() {
        items.clear()
        items.putAll(ConfigManager.load(ItemsPlugin.plugin))
    }

    fun createItem(item: ConfigurationItem, defaults: Map<String, Pair<String, ItemProperty.TypeStorage>>) : ItemStack {
        val stack = VersionRegistry.version.buildItem(item)

        val placeholders = mutableMapOf<String, String>()

        NBT.modify(stack) {
            if (item.properties.isNotEmpty())
                item.properties.forEach { (prop: ItemProperty, value: Any) -> prop.set(it, value) }

            defaults.forEach { key, (value, type) ->
                type.setToItem(it, key, type.parse(value))
            }
        }

        NBT.get(stack) {

            item.properties.keys.mapNotNull { h -> actions[h] }.linearise().forEach { t ->
                (t as? PlaceholdersItemPropertyAction)?.generate(it, placeholders)
            }

            val meta = stack.itemMeta
            NameChangeAction.applyToCurrentLore(meta, placeholders.entries)
            LoreChangeAction.applyToCurrentLore(meta, placeholders.entries)
            stack.itemMeta = meta

            ItemActionManager.updateItem(it, stack, placeholders)
        }

        return stack
    }
}