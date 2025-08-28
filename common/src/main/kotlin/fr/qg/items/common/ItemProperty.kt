package fr.qg.items.common

import de.tr7zw.changeme.nbtapi.iface.ReadWriteItemNBT
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.models.WriteableNBT
import org.bukkit.configuration.ConfigurationSection

enum class ItemProperty(
    val type: ItemPropertyType,
    val storage: TypeStorage,
) {

    // the item will stay in your inventory after your death
    INFINITE(ItemPropertyType.PRIMARY, TypeStorage.BOOLEAN),

    // you will have an action when shiftcliking in the items
    // actions are a list, with prefix, as instance
    // [console] execute a command in the console
    // [player] is a command by the player
    // you have by default the %player% placeholder
    SHIFT_CLICK_ACTION(ItemPropertyType.PRIMARY, TypeStorage.LIST),

    // is used to have more placeholder's when you shift click
    // as instance you can get statistics, durability or the owner tag
    // ENCODAGE :
    // it's a string, each placeholder are splat using space and they are followed by a $ and their type
    // as instance :
    // "owner$string use$int" is a valid encodage
    CLICKS_PLACEHOLDERS(ItemPropertyType.LINKED, TypeStorage.STRING),

    // chop a tree
    TREE_CHOPPER(ItemPropertyType.PRIMARY, TypeStorage.BOOLEAN),

    // the destroy/replant radius of your item
    HARVEST_RADIUS(ItemPropertyType.PRIMARY, TypeStorage.INT),

    // the item's multiplier of your harvest hoe
    // you must have HARVEST_RADIUS for this to work
    HARVEST_FORTUNE(ItemPropertyType.LINKED, TypeStorage.INT),

    // if the harvest hoe is selling or not
    // you must have HARVEST_RADIUS or TREE_CHOP for this to work
    AUTOSELL(ItemPropertyType.LINKED, TypeStorage.BOOLEAN),

    // your pickaxe will break with a radius, as instance 3x3 (1) or 5x5(2)
    HAMMER(ItemPropertyType.PRIMARY, TypeStorage.INT),

    // durability function, when 0 your item will break
    USE(ItemPropertyType.FEATURE, TypeStorage.INT),

    // ability to store statistics, enter a list of types,
    // as instance crops or blocks
    // and the item will store theses stats,
    // usefull for click action (to setup a level system)
    // and for a beautiful lore
    STATISTICS(ItemPropertyType.FEATURE, TypeStorage.LIST),

    // store the owner of the item
    OWNER(ItemPropertyType.FEATURE, TypeStorage.STRING),

    // update the name on each action, usefull for statistics or things like that
    NAME_CHANGE(ItemPropertyType.ITEM_UPDATE, TypeStorage.STRING),

    // update the lore on each action, usefull for statistics or things like that
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