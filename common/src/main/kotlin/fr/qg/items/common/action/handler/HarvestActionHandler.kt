package fr.qg.items.common.action.handler

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.models.WriteableNBT
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player

interface HarvestActionHandler {

    fun handle(player: Player, nbt: ReadableItemNBT, writer: WriteableNBT, block: Block, radius: Int, fortune: Int) : Map<Material, Int>
}