package fr.qg.items.action

import de.tr7zw.changeme.nbtapi.NBT
import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.action.ItemPropertyAction
import fr.qg.items.common.action.operate
import fr.qg.items.common.managers.ItemActionManager
import fr.qg.items.common.models.WriteableNBT
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object ShiftClickAction : Listener, ItemPropertyAction {

    @EventHandler
    fun onShiftClick(e: PlayerInteractEvent) {
        val player = e.player
        if (!player.isSneaking) return
        val item = e.item ?: return
        executeShiftActions(player, item)
    }

    private fun executeShiftActions(player: Player, item: ItemStack) {
        NBT.get(item) { nbt ->
            if (!ItemProperty.SHIFT_CLICK_ACTION.has(nbt)) return@get

            val actions = ItemProperty.SHIFT_CLICK_ACTION.get<List<String>>(nbt)
            val placeholders = mutableMapOf<String, String>("%player%" to player.name, "&" to "§")

            if (ItemProperty.CLICKS_PLACEHOLDERS.has(nbt)) {
                val keyStr = ItemProperty.CLICKS_PLACEHOLDERS.get<String>(nbt)
                keyStr.split(" ").forEach {
                    val keys = it.split("$")
                    val name = keys[0]
                    val type = runCatching {
                        ItemProperty.TypeStorage.valueOf(keys[1].uppercase())
                    }.getOrDefault(ItemProperty.TypeStorage.STRING)

                    placeholders[name] = type.getFromItem(nbt, name).toString()
                }
            }

            actions.forEach { parseAndExecute(player, placeholders.entries.operate(it)
                    { s, k, v -> s.replace("%$k%", v) })
            }
        }
    }

    private fun parseAndExecute(player: Player, action: String) {
        when {
            action.startsWith("[player]") -> {
                val cmd = action.removePrefix("[player]").trim()
                player.performCommand(cmd)
            }
            action.startsWith("[console]") -> {
                val cmd = action.removePrefix("[console]").trim()
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
            }
            action.startsWith("[message]") -> {
                val msg = action.removePrefix("[message]").trim()
                player.sendMessage(msg)
            }
            action.startsWith("[sound]") -> {
                val soundName = action.removePrefix("[sound]").trim().uppercase()
                runCatching {
                    val sound = Sound.valueOf(soundName)
                    player.playSound(player.location, sound, 1f, 1f)
                }
            }
        }
    }
}
