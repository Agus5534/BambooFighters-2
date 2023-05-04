package io.github.agus5534.bamboofightersv2.commands;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.utils.location.LocationUtil;
import io.github.agus5534.utils.command.CommandConstructor;
import io.github.agus5534.utils.command.annotations.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;


@Command(
        name = "convertlocation",
        help = "/<command>",
        description = "Convierte tu localización actual a una deserializada",
        permission = "bamboofighters.staff",
        permissionDefault = PermissionDefault.OP,
        senders = CommandConstructor.SenderType.PLAYER
)
public class ConvertLocationCommand extends CommandConstructor {

    public ConvertLocationCommand() {
        super();
    }

    @Override
    protected void execute() {
        var pl = (Player)sender;

        var loc = LocationUtil.deserialize(pl.getLocation());
        Component locationDeserialized = Component.text(loc).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, loc));

        BambooFighters.convertedLocations.add(loc);
        sender.sendMessage(locationDeserialized);

        if(sender.getName().equalsIgnoreCase("Agus5534")) {
            Component t = Component.text("Click para copiar todas las locs convertidas!");
            StringBuilder s = new StringBuilder();

            var it = BambooFighters.convertedLocations.iterator();

            while (it.hasNext()) {
                s.append("\"").append(it.next()).append("\",");
            }

            sender.sendMessage(t.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, s.toString())));
        }
    }
}
