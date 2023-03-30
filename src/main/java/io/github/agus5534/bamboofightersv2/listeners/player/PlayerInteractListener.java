package io.github.agus5534.bamboofightersv2.listeners.player;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.PHYSICAL)) { return; }
        if(event.getClickedBlock() == null) { return; }
        if(!event.getClickedBlock().getType().equals(Material.FARMLAND)) { return; }

        event.setCancelled(true);
    }
}
