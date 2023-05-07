package io.github.agus5534.bamboofightersv2.commands.manager;

import io.github.agus5534.bamboofightersv2.commands.list.GenericCommands;
import io.github.agus5534.bamboofightersv2.commands.list.TeamCommands;
import io.github.agus5534.bamboofightersv2.commands.manager.module.ExtraBukkitModule;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;

public class CommandManager {

    public void load() {
        registerCommands(
                new GenericCommands(),
                new TeamCommands()
        );
    }
    private void registerCommands(CommandClass... commandClasses) {
        PartInjector partInjector = PartInjector.create();
        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());
        partInjector.install(new ExtraBukkitModule());

        BukkitCommandManager commandManager = new BukkitCommandManager("bf2");
        AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(partInjector);

        for (CommandClass commandClass : commandClasses) {
            commandManager.registerCommands(treeBuilder.fromClass(commandClass));
        }
    }
}
