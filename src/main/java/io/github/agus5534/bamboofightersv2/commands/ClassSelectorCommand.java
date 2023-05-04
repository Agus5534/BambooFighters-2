package io.github.agus5534.bamboofightersv2.commands;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.menus.ClassSelectionMenu;
import io.github.agus5534.utils.command.CommandConstructor;
import io.github.agus5534.utils.command.annotations.Command;
import io.github.agus5534.utils.text.ComponentManager;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.lang.instrument.IllegalClassFormatException;

@Command(
        name = "selectclass",
        help = "/<command>",
        description = "Elige tu clase",
        senders = CommandConstructor.SenderType.PLAYER,
        permission = "bamboofighters.basicCommands",
        permissionDefault = PermissionDefault.TRUE
)
public class ClassSelectorCommand extends CommandConstructor {

    BambooFighters plugin;

    public ClassSelectorCommand(BambooFighters plugin) {
        super();

        this.plugin = plugin;
    }

    @Override
    protected void execute() {
        if(BambooFighters.getActualGameCombat() == null) {
            sender.sendMessage(ComponentManager.formatMiniMessage("<red>No hay ningún combate en progreso"));
            return;
        }

        var playerTeam = BambooFighters.playerGameTeamHashMap.get((Player)sender);

        if(playerTeam == null) {
            sender.sendMessage(TranslatableText.basicTranslate("command.selectclass.missing_team"));
            return;
        }

        if(BambooFighters.getActualGameCombat().isStarted()) {
            sender.sendMessage(TranslatableText.basicTranslate("command.selectclass.game_running"));
            return;
        }

        var pl = (Player) sender;

        pl.openInventory(new ClassSelectionMenu(plugin).getInventory());
    }
}
