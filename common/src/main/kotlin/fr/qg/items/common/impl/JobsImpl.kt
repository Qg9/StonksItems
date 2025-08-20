package fr.qg.items.common.impl

import org.bukkit.block.Block
import org.bukkit.entity.Player

interface JobsImpl {

    fun actions(player: Player, block: Block)
}