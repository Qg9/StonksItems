package fr.qg.items.common.models

import fr.qg.items.common.ItemProperty
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag

data class ConfigurationItem(
    val material: Material,
    val name: String? = null,
    val lore: List<String> = emptyList(),
    val modelData: Int? = null,
    val enchantments: Map<Enchantment, Int> = emptyMap(),
    val itemFlags: Set<ItemFlag> = emptySet(),

    val unbreakable: Boolean = false,
    val customDurability: Int? = null,

    val attributeModifiers: Map<String, ConfigurationSection> = emptyMap(),

    val properties: Map<ItemProperty, Any> = emptyMap()
) {
    fun prettyPrint(): String {
        return buildString {
            appendLine("ConfigurationItem {")
            appendLine("  material = $material")
            appendLine("  name = $name")
            appendLine("  lore = ${if (lore.isEmpty()) "[]" else lore.joinToString(prefix = "[", postfix = "]")}")
            appendLine("  modelData = $modelData")
            appendLine("  enchantments = ${if (enchantments.isEmpty()) "{}" else enchantments.entries.joinToString { "${it.key.name}:${it.value}" }}")
            appendLine("  itemFlags = ${if (itemFlags.isEmpty()) "{}" else itemFlags.joinToString(", ")}")
            appendLine("  unbreakable = $unbreakable")
            appendLine("  customDurability = $customDurability")
            appendLine("  attributeModifiers = ${if (attributeModifiers.isEmpty()) "{}" else attributeModifiers.keys}")
            appendLine("  properties = ${if (properties.isEmpty()) "{}" else properties.entries.joinToString { "${it.key}=${it.value}" }}")
            appendLine("}")
        }
    }
}
