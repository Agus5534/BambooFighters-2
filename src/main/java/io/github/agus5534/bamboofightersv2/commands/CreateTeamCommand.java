package io.github.agus5534.bamboofightersv2.commands;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.utils.command.CommandConstructor;
import io.github.agus5534.utils.command.annotations.utils.RequiresPlayerArgument;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collection;
import java.util.Collections;

@RequiresPlayerArgument
public class CreateTeamCommand extends CommandConstructor {

    private BambooFighters plugin;
    public CreateTeamCommand(BambooFighters plugin) {
        super(
                "createteam",
                "/<command> [Player]",
                "Crea un team",
                "bamboofighters.staff",
                "§4Missing permission",
                PermissionDefault.OP,
                new SenderType[]{SenderType.PLAYER},
                true
        );
        this.plugin = plugin;
    }

    @Override
    protected void execute() {
        var pl = Bukkit.getPlayer(args[0]);

        var playerTeam = BambooFighters.playerGameTeamHashMap.get(pl);

        if(playerTeam != null) {
            if(playerTeam.getOwner() == pl) {
                sender.sendMessage(TranslatableText.basicTranslate("command.createteam_already_created"));
                return;
            }

            sender.sendMessage(TranslatableText.basicTranslate("command.createteam_already_inteam"));
            return;
        }

        var team = new GameTeam(pl);

        sender.sendMessage(TranslatableText.basicTranslate("command.createteam_created_successfuly",pl.getName()));

        var color = plugin.getColors().stream().findAny().get();

        team.setColor(color);

        plugin.getColors().remove(color);
    }
}
