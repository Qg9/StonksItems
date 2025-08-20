package fr.qg.items.common.impl.jobs

import com.gamingmesh.jobs.Jobs
import com.gamingmesh.jobs.actions.BlockActionInfo
import com.gamingmesh.jobs.container.ActionType
import fr.qg.items.common.impl.JobsImpl
import org.bukkit.block.Block
import org.bukkit.entity.Player

object JobsRebornImpl : JobsImpl {

    override fun actions(player: Player, block: Block) {
        val jp = Jobs.getPlayerManager().getJobsPlayer(player)
        Jobs.action(jp, BlockActionInfo(block, ActionType.BREAK))
    }
}