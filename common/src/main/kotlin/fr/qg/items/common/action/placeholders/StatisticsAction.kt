package fr.qg.items.common.action.placeholders

import de.tr7zw.changeme.nbtapi.iface.ReadableItemNBT
import fr.qg.items.common.ItemProperty
import fr.qg.items.common.models.WriteableNBT

object StatisticsAction : PlaceholdersItemPropertyAction {

    const val STATS_PREFIX = "stats_"

    fun apply(nbt: ReadableItemNBT, writer: WriteableNBT, placeholders: MutableMap<String, String>, statsGetter: () -> Map<String, Int>) {
        if (!ItemProperty.STATISTICS.has(nbt)) return
        val stats = statsGetter.invoke()

        val keys = ItemProperty.STATISTICS.get<List<String>>(nbt)
        writer.write {

            for (statKey in keys) {
                val increment = stats[statKey] ?: 0
                val current = nbt.getInteger("$STATS_PREFIX$statKey")
                val updated = current + increment

                it.setInteger("$STATS_PREFIX$statKey", updated)

                placeholders[statKey] = updated.toString()
            }
        }
    }

    override fun generate(nbt: ReadableItemNBT, placeholders: MutableMap<String, String>) =
        apply(nbt, WriteableNBT(), placeholders, {mapOf()})
}