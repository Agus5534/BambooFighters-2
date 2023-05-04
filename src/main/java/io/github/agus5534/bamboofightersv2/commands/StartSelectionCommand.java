package io.github.agus5534.bamboofightersv2.commands;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import io.github.agus5534.utils.command.CommandConstructor;
import io.github.agus5534.utils.command.annotations.Command;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;

import java.util.concurrent.atomic.AtomicInteger;

@Command(
        name = "startselection",
        help = "/<command> [tier]",
        description = "Inicia la selección de jugadores",
        permissionDefault = PermissionDefault.OP,
        permission = "bamboofighters.staff",
        senders = CommandConstructor.SenderType.PLAYER
)
public class StartSelectionCommand extends CommandConstructor {
    BambooFighters plugin;

    public StartSelectionCommand(BambooFighters plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    protected void execute() {
        try {
            Integer i = Integer.parseInt(args[0]);

            var tier = this.getTier(i);

            if(tier == null) {
                sender.sendMessage("Invalid Tier Number. Valid from One to Four (1 -> 4)");
                return;
            }

            var sel = new PlayerSelection(tier, BambooFighters.getGameTeams(), plugin);
            sel.startSelection();
        } catch (Exception e) {
            sender.sendMessage("Invalid Tier Number. Valid from One to Four (1 -> 4)");
        }
    }

    public PlayerSelection.SelectionTier getTier(int n) {
        switch (n) {
            case 1 -> {
                return PlayerSelection.SelectionTier.TIER_1;
            }
            case 2 -> {
                return PlayerSelection.SelectionTier.TIER_2;
            }
            case 3 -> {
                return PlayerSelection.SelectionTier.TIER_3;
            }
            case 4 -> {
                return PlayerSelection.SelectionTier.TIER_4;
            }
            default -> {
                return null;
            }

        }
    }
}
