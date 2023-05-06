package io.github.agus5534.bamboofightersv2.commands;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.menus.CombatSelectionMenu;
import io.github.agus5534.utils.command.CommandConstructor;
import io.github.agus5534.utils.command.annotations.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

@Command(
        name = "startcombat",
        help = "/<command>",
        description = "Inicia un combate",
        permissionDefault = PermissionDefault.OP,
        permission = "bamboofighters.staff",
        senders = CommandConstructor.SenderType.PLAYER
)
public class CombatCommand extends CommandConstructor {

    BambooFighters plugin;

    public CombatCommand(BambooFighters plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    protected void execute() {
        if(BambooFighters.getActualGameCombat() != null) {
            var combat = BambooFighters.getActualGameCombat();

            Bukkit.getScheduler().cancelTask(combat.getMainTask());
            combat.forceEnd();
        } else {
            ((Player)sender).openInventory(new CombatSelectionMenu(plugin).getArenaSelector());
        }

    }
}
