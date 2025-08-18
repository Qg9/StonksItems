package fr.qg.items.commands

import com.jonahseguin.drink.argument.CommandArg
import com.jonahseguin.drink.parametric.DrinkProvider
import fr.qg.items.common.models.ConfigurationItem
import fr.qg.items.managers.ItemsManager

object ConfigurationItemProvider : DrinkProvider<ConfigurationItem>() {
    override fun doesConsumeArgument(): Boolean = true

    override fun isAsync(): Boolean = false

    override fun provide(arg: CommandArg, annotations: List<Annotation?>): ConfigurationItem? =
        ItemsManager.items[arg.get()]

    override fun argumentDescription(): String? = "ItemConfiguration"

    override fun getSuggestions(prefix: String): List<String?>? =
        ItemsManager.items.keys.filter { prefix.isEmpty() || it.startsWith(prefix, true) }
}