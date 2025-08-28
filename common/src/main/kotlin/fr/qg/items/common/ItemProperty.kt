package fr.qg.items.common

import de.tr7zw.changeme.nbtapi.iface.ReadWriteItemNBT
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.models.WriteableNBT
import org.bukkit.configuration.ConfigurationSection

enum class ItemProperty(
    val type: ItemPropertyType,
    val storage: TypeStorage,
) {

    INFINITE(ItemPropertyType.PRIMARY, TypeStorage.BOOLEAN),
    SHIFT_CLICK_ACTION(ItemPropertyType.PRIMARY, TypeStorage.LIST),
    CLICKS_PLACEHOLDERS(ItemPropertyType.LINKED, TypeStorage.LIST),

    HARVEST_RADIUS(ItemPropertyType.PRIMARY, TypeStorage.INT),
    HARVEST_FORTUNE(ItemPropertyType.LINKED, TypeStorage.INT),
    HARVEST_AUTOSELL(ItemPropertyType.LINKED, TypeStorage.BOOLEAN),

    HAMMER(ItemPropertyType.PRIMARY, TypeStorage.INT),

    USE(ItemPropertyType.FEATURE, TypeStorage.INT),
    STATISTICS(ItemPropertyType.FEATURE, TypeStorage.LIST),
    OWNER(ItemPropertyType.FEATURE, TypeStorage.STRING),

    NAME_CHANGE(ItemPropertyType.ITEM_UPDATE, TypeStorage.STRING),
    LORE_CHANGE(ItemPropertyType.ITEM_UPDATE, TypeStorage.LIST)
    ;

    companion object {
        const val PREFIX_ITEMS = "items_"
    }

    val key: String
        get() = "$PREFIX_ITEMS${name.lowercase()}"

    val placeholderkey: String
        get() = name.lowercase()

    fun <T> get(nbt: ReadableItemNBT): T =
        this.storage.getFromItem(nbt, this.key) as? T ?: error("code error from casting contact Qg")

    fun has(nbt: ReadableItemNBT): Boolean = this.storage.has(nbt, this.key)

    fun set(nbt: ReadWriteItemNBT, value: Any?) =
        value?.let {
            println("set ${this.key} with $it to $nbt")
            this.storage.setToItem(nbt, this.key, it) }

    fun set(nbt: WriteableNBT, value: Any?) = nbt.write { w ->
        value?.let {
            this.storage.setToItem(w, this.key, it) }
    }

    enum class ItemPropertyType {
        PRIMARY,
        LINKED,
        FEATURE,
        ITEM_UPDATE
    }

    enum class TypeStorage(
        val setToItem: (ReadWriteItemNBT, String, Any) -> Unit,
        val getFromConfig: (ConfigurationSection, String) -> Any,
        val getFromItem: (ReadableItemNBT, String) -> Any,
        val has: (ReadableItemNBT, String) -> Boolean,
        val parse: (String) -> Any
    ) {
        INT(
            { it, s, v -> it.setInteger(s, v as Int) },
            { c, s -> c.getInt(s) },
            { i, s -> i.getInteger(s) },
            { i, s -> i.hasTag(s) },
            { it.toInt() }
        ),
        STRING(
            { it, s, v -> it.setString(s, v as String) },
            { c, s -> c.getString(s, "").replace("&", "§")},
            { i, s -> i.getString(s) ?: "" },
            { i, s -> i.hasTag(s) },
            { it }
        ),
        BOOLEAN(
            { it, s, v -> it.setBoolean(s, v as Boolean) },
            { c, s -> c.getBoolean(s, false) },
            { i, s -> i.getBoolean(s) },
            { i, s -> i.hasTag(s) },
            { it.toBoolean() }
        ),
        LIST(
            { it, s, v -> it.setString(s, (v as List<*>).joinToString("\\!")) },
            { c, s -> c.getStringList(s).map { it.replace("&", "§") } },
            { i, s -> LIST.parse(i.getString(s)) },
            { i, s -> i.hasTag(s) },
            { it.split("\\!").filter { it.isNotEmpty() } }
        )
    }
}