package io.github.agus5534.bamboofightersv2.commands.list;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

@Command(
        names = "arena",
        permission = "bamboofighters.staff"
)
public class ArenaCommands implements CommandClass {

    @Command(
            names = "create"
    )
    public void createArgument(@Sender Player sender, @Named("name") String name) {

    }
}
