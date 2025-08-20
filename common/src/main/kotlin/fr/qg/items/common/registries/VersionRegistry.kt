package fr.qg.items.common.registries

import fr.qg.items.common.impl.VersionImpl
import org.bukkit.Bukkit

object VersionRegistry {

    enum class PluginVersion(val versionPrefix: String, val implClass: String) {
        V8("1.8", "fr.qg.items.v8.VersionImplementation8"),
        V21("1.21", "fr.qg.items.v21.VersionImplementation21")
    }

    lateinit var version: VersionImpl

    private fun serverBukkitVersion(): String =
        runCatching { Bukkit.getBukkitVersion() }.getOrNull() ?: ""

    private fun resolveVersion(): PluginVersion {
        val v = serverBukkitVersion()
        return PluginVersion.entries.firstOrNull { v.startsWith(it.versionPrefix) }
            ?: error("Unsupported Bukkit version: '$v'")
    }

    fun load()  {
        val implClass = Class.forName(resolveVersion().implClass)
        val ctor = implClass.getDeclaredConstructor()
        ctor.isAccessible = true
        version = ctor.newInstance() as VersionImpl
    }
}