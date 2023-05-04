package io.github.agus5534.bamboofightersv2.commands;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.utils.command.CommandConstructor;
import io.github.agus5534.utils.command.annotations.Command;
import io.github.agus5534.utils.text.ComponentManager;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;

import java.lang.instrument.IllegalClassFormatException;
import java.util.concurrent.atomic.AtomicInteger;

@Command(
        name = "restartserver",
        help = "/<command> [ticks]",
        description = "Reinicia el servidor en un tiempo determinado",
        permissionDefault = PermissionDefault.OP,
        permission = "bamboofighters.staff",
        senders = CommandConstructor.SenderType.CONSOLE
)
public class RestartServerCommand extends CommandConstructor {

    BambooFighters plugin;
    Integer tskID;

    public RestartServerCommand(BambooFighters plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    protected void execute() {
        try {
            Integer i = Integer.parseInt(args[0]);
            AtomicInteger ticks = new AtomicInteger();

            ticks.set(i);
            if(i > 24000 || i < 1200) {
                sender.sendMessage("No se permite mas de 20 minutos.");
                return;
            }

            if(tskID != null) {
                sender.sendMessage("Ya hay un restart pendiente.");
                return;
            }

            Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                Bukkit.getScheduler().cancelTask(tskID);
                tskID = null;
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
            }, i);

            tskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->{
                ticks.getAndDecrement();

                long seconds = ticks.get() / 20;

                int timerMins = (int) seconds / 60;
                int timerSecs = (int) seconds % 60;

                String restartMins = String.format("%02d", timerMins);
                String restartSeconds = String.format("%02d", timerSecs);
                Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(TranslatableText.basicTranslate("warn.server_restarts", restartMins, restartSeconds)));
            }, 1L, 1L);
        } catch (Exception e) {
            return;
        }
    }
}
