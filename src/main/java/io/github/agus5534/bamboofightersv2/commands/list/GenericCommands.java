package io.github.agus5534.bamboofightersv2.commands.list;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.menus.ClassSelectionMenu;
import io.github.agus5534.bamboofightersv2.menus.CombatSelectionMenu;
import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import io.github.agus5534.bamboofightersv2.utils.extra.TimeFormatter;
import io.github.agus5534.bamboofightersv2.utils.extra.Validate;
import io.github.agus5534.bamboofightersv2.utils.location.LocationUtil;
import io.github.agus5534.utils.text.ChatFormatter;
import io.github.agus5534.utils.text.TranslatableText;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class GenericCommands implements CommandClass {
    private Integer taskID = null;

    @Command(
            names = "convertlocation",
            permission = "bamboofighters.staff"
    )
    public void convertLocationCommand(@Sender Player sender) {
        var loc = LocationUtil.deserialize(sender.getLocation());
        Component locationDeserialized = Component.text(loc).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, loc));

        BambooFighters.convertedLocations.add(loc);
        sender.sendMessage(locationDeserialized);
    }

    @Command(
            names = "programrestart",
            permission = "bamboofighters.staff"
    )
    public void programRestartCommand(@Sender Player sender, @Named("time") TimeFormatter timeFormatter) {
        AtomicInteger ticks = new AtomicInteger((int) timeFormatter.convertTo(TimeFormatter.Format.TICKS));

        if(ticks.get() < 600) {
            sender.sendMessage(ChatFormatter.formatMiniMessage("<red>No se puede colocar menos de 30 segundos."));
            return;
        }

        if(!Validate.isNull(taskID)) {
            sender.sendMessage(ChatFormatter.formatMiniMessage("<red>Ya hay un reinicio pendiente."));
            return;
        }

        Bukkit.getScheduler().runTaskLater(BambooFighters.instance, ()-> {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = null;
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, ticks.get());

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(BambooFighters.instance, ()-> {
            ticks.getAndDecrement();

            long seconds = ticks.get() / 20;

            int timerMins = (int) seconds / 60;
            int timerSecs = (int) seconds % 60;

            String restartMins = String.format("%02d", timerMins);
            String restartSeconds = String.format("%02d", timerSecs);
            Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(TranslatableText.basicTranslate("warn.server_restarts", restartMins, restartSeconds)));
        }, 1L, 1L);
    }

    @Command(
            names = { "selectclass", "class" }
    )
    public void selectClassCommand(@Sender Player sender) {
        var combat = BambooFighters.getActualGameCombat();

        if(Validate.isNull(combat)) {
            sender.sendMessage(ChatFormatter.formatMiniMessage("<red>No hay ningún combate en progreso"));
            return;
        }

        var playerTeam = BambooFighters.playerGameTeamHashMap.get(sender);

        if(Validate.isNull(playerTeam)) {
            sender.sendMessage(TranslatableText.basicTranslate("command.selectclass.missing_team"));
            return;
        }

        if(combat.isStarted()) {
            sender.sendMessage(TranslatableText.basicTranslate("command.selectclass.game_running"));
            return;
        }

        sender.openInventory(new ClassSelectionMenu(BambooFighters.instance).getInventory());
    }

    @Command(
            names = "startcombat",
            permission = "bamboofighters.staff"
    )
    public void startCombatCommand(@Sender Player sender) {
        var combat = BambooFighters.getActualGameCombat();

        if(!Validate.isNull(combat)) {
            Bukkit.getScheduler().cancelTask(combat.getMainTask());
            combat.forceEnd();

            return;
        }

        sender.openInventory(new CombatSelectionMenu(BambooFighters.instance).getArenaSelector());
    }

    @Command(
            names = { "startselection", "beginselection", "selection" },
            permission = "bamboofighters.staff"
    )
    public void startSelectionCommand(@Sender Player sender, @Named("tier") PlayerSelection.SelectionTier tier) {
        var sel = new PlayerSelection(tier, BambooFighters.getGameTeams(), BambooFighters.instance);

        sel.startSelection();
    }
}
