package fr.qg.items

import fr.qg.items.common.VersionImplementation
import org.bukkit.Bukkit

object VersionLoader {

    enum class PluginVersion(val versionPrefix: String, val implClass: String) {
        V8("1.8", "fr.qg.items.v8.VersionImplementation8"),
        V21("1.21", "fr.qg.items.v21.VersionImplementation21")
    }

    private fun serverBukkitVersion(): String =
        runCatching { Bukkit.getBukkitVersion() }.getOrNull() ?: ""

    private fun resolveVersion(): PluginVersion {
        val v = serverBukkitVersion()
        return PluginVersion.entries.firstOrNull { v.startsWith(it.versionPrefix) }
            ?: error("Unsupported Bukkit version: '$v'")
    }

    fun load(): VersionImplementation {
        val implClass = Class.forName(resolveVersion().implClass)
        val ctor = implClass.getDeclaredConstructor()
        ctor.isAccessible = true
        return ctor.newInstance() as VersionImplementation
    }
}