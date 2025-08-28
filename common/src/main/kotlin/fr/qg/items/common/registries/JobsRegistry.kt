package fr.qg.items.common.registries

import fr.qg.items.common.impl.JobsImpl
import org.bukkit.plugin.java.JavaPlugin

object JobsRegistry {

    enum class JobsType(val plugin: String, val implClass: String) {
        JOBS_REBORN("Jobs", "fr.qg.items.common.impl.jobs.JobsRebornImpl"),
        NONE("", "fr.qg.items.common.impl.jobs.NoJobsImpl"),
    }

    lateinit var jobs: JobsImpl

    fun resolveVersion(plugin: JavaPlugin) : JobsType =
        JobsType.entries.firstOrNull { plugin.server.pluginManager.getPlugin(it.plugin) != null }
            ?: JobsType.NONE

    fun load(plugin: JavaPlugin)  {
        val version = resolveVersion(plugin)
        val implClass = Class.forName(version.implClass)
        val ctor = implClass.getDeclaredConstructor()
        ctor.isAccessible = true
        jobs = ctor.newInstance() as JobsImpl

        println("Loaded ${version.name} as Jobs Hook !")
    }
}