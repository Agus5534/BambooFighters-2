package io.github.agus5534.bamboofightersv2.listeners.player;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendPlayerListHeader(BambooFighters.tabHeader);
    }
}
