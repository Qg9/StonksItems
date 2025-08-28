package fr.qg.items.common.managers

import fr.qg.items.common.ItemProperty
import fr.qg.items.common.models.ConfigurationItem
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin

object ConfigManager {

    fun loadItem(section: ConfigurationSection): ConfigurationItem {
        val material = Material.valueOf(section.getString("material", "STONE")!!.uppercase())
        val name = section.getString("name")
        val lore = section.getStringList("lore")
        val modelData = section.getInt("model-data").takeIf { section.contains("model-data") }

        val enchantments = mutableMapOf<Enchantment, Int>()
        section.getConfigurationSection("enchantments")?.let { enchSection ->
            for (key in enchSection.getKeys(false)) {
                Enchantment.getByName(key.uppercase())?.let { enchantments[it] = enchSection.getInt(key) }
            }
        }

        val flags = mutableSetOf<ItemFlag>()
        section.getStringList("flags").forEach { n ->
            ItemFlag.entries.find { it.name.equals(n, ignoreCase = true) }?.let { flags += it }
        }

        val unbreakable = section.getBoolean("unbreakable", false)
        val customDurability = section.getInt("custom-durability").takeIf { section.contains("custom-durability") }

        val attributeModifiers = section.getConfigurationSection("attributes")
            ?.getValues(false)
            ?.mapNotNull { (key, _) ->
                val modifierSection = section.getConfigurationSection("attributes.$key")
                if (modifierSection != null) key to modifierSection else null
            }?.toMap() ?: emptyMap()

        val properties = mutableMapOf<ItemProperty, Any>()
        section.getConfigurationSection("properties")?.let { props ->
            for (key in props.getKeys(false)) {
                ItemProperty.entries.find{ it.name.equals(key, true) }?.let { property ->
                    val value = property.storage.getFromConfig(props, key)
                    properties[property] = value
                } ?: error("unrecognized attribute $key for ${section.name}")
            }
        }

        return ConfigurationItem(
            material = material,
            name = name,
            lore = lore,
            modelData = modelData,
            enchantments = enchantments,
            itemFlags = flags,
            unbreakable = unbreakable,
            customDurability = customDurability,
            attributeModifiers = attributeModifiers,
            properties = properties
        )
    }

    fun load(plugin: JavaPlugin): Map<String, ConfigurationItem> {
        val items = plugin.config.getConfigurationSection("items") ?: return emptyMap()
        return items.getKeys(false)
            .mapNotNull { key -> items.getConfigurationSection(key)
                ?.let { key.lowercase() to loadItem(it) } }
            .toMap().mapKeys {
                println("LOADED ITEMS : ${it.key}")
                it.key
            }
    }
}