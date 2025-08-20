package fr.qg.items.common.impl.jobs

import fr.qg.items.common.impl.JobsImpl
import org.bukkit.block.Block
import org.bukkit.entity.Player

object NoJobsImpl : JobsImpl {
    override fun actions(player: Player, block: Block) {}
}