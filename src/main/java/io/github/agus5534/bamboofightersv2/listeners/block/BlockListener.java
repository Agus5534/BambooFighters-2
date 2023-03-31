package io.github.agus5534.bamboofightersv2.listeners.block;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }

        if(event.getBlock().getType() != Material.WHITE_WOOL && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            if(event.getBlock().getType() != Material.FIRE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }

        var b = event.getBlock();
        var bType = b.getType();

        if(bType == Material.WHITE_WOOL || bType == Material.FIRE || event.getItemInHand().getType() == Material.FLINT_AND_STEEL) {
            return;
        }

        event.setCancelled(true);
    }
}
