package fr.qg.items.v8.harvest

import org.bukkit.Material
import org.bukkit.block.Block
import java.util.function.Predicate

enum class HarvestType(
    val blockType: Material,
    val dropType: Material,
    val minAmount: Int,
    val maxAmount: Int,
    val isMature: Predicate<Block>,
    val reset: Block.() -> Unit
) {

    WHEAT(
        Material.CROPS, Material.WHEAT, 1, 1,
        Predicate { it.data.toInt() == 7 },
        { type = Material.CROPS; data = 0.toByte() }
    ),

    CARROT(
        Material.CARROT, Material.CARROT_ITEM, 1, 3,
        Predicate { it.data.toInt() == 7 },
        { type = Material.CARROT; data = 0.toByte() }
    ),

    POTATO(
        Material.POTATO, Material.POTATO_ITEM, 1, 4,
        Predicate { it.data.toInt() == 7 },
        { type = Material.POTATO; data = 0.toByte() }
    ),

    NETHER_WART(
        Material.NETHER_WARTS, Material.NETHER_STALK, 2, 4,
        Predicate { it.data.toInt() == 3 },
        { type = Material.NETHER_WARTS; data = 0.toByte() }
    ),

    SUGAR_CANE(
        Material.SUGAR_CANE_BLOCK, Material.SUGAR_CANE, 1, 1,
        Predicate { true },
        { type = Material.AIR }
    ),

    CACTUS(
        Material.CACTUS, Material.CACTUS, 1, 1,
        Predicate { true },
        { type = Material.AIR }
    ),

    MELON(
        Material.MELON_BLOCK, Material.MELON, 3, 7,
        Predicate { true },
        { type = Material.AIR }
    ),

    PUMPKIN(
        Material.PUMPKIN, Material.PUMPKIN, 1, 1,
        Predicate { true },
        { type = Material.AIR }
    );

    companion object {
        fun fromBlock(type: Material): HarvestType? =
            values().find { it.blockType == type }
    }
}
