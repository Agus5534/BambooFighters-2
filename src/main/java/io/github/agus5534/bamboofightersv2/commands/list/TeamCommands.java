package io.github.agus5534.bamboofightersv2.commands.list;

import io.github.agus5534.agusutils.utils.text.ChatFormatter;
import io.github.agus5534.agusutils.utils.text.TranslatableText;
import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.bamboofightersv2.utils.extra.Validate;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

@Command(
        names = "team",
        permission = "bamboofighters.staff"
)
public class TeamCommands implements CommandClass {

    @Command(names = "save")
    public void saveArgument(@Sender Player sender) {
        var s = BambooFighters.instance.saveTeams();

        sender.sendMessage(ChatFormatter.formatMiniMessage(String.format("<green>Se han guardado los Teams en la carpeta <gold>teams/%s/", s)));
    }

    @Command(names = "create")
    public void createArgument(@Sender Player sender, @Named("owner") Player owner) {
        var playerTeam = BambooFighters.playerGameTeamHashMap.get(owner);
        var plugin = BambooFighters.instance;

        if(!Validate.isNull(playerTeam)) {
            if(playerTeam.getOwner() == owner) {
                sender.sendMessage(TranslatableText.basicTranslate("command.createteam_already_created"));
                return;
            }

            sender.sendMessage(TranslatableText.basicTranslate("command.createteam_already_inteam"));
            return;
        }

        var team = new GameTeam(owner);

        sender.sendMessage(TranslatableText.basicTranslate("command.createteam_created_successfuly", sender.getName()));

        var color = plugin.getColors().stream().findAny().orElse(NamedTextColor.BLACK);

        team.setColor(color);
        plugin.getColors().remove(color);
    }
}
