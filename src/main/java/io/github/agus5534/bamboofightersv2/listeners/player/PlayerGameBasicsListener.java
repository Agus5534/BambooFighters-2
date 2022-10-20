package io.github.agus5534.bamboofightersv2.listeners.player;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerGameBasicsListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        player.setGameMode(GameMode.SPECTATOR);

        player.setBedSpawnLocation(player.getLocation());
    }

    @EventHandler
    public void onTake(PlayerTakeLecternBookEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            if(event.getPlayer().getInventory().getItemInMainHand() != null) {
                if(event.getPlayer().getInventory().getItemInMainHand().getType().isBlock()) {
                    return;
                }
                if(event.getPlayer().getInventory().getItemInMainHand().getType().isItem()) {
                    return;
                }
            }
            if(event.getItem() != null) {
                if(!event.getItem().isSimilar(new ItemStack(Material.FLINT_AND_STEEL))) {
                    event.setCancelled(true);
                }
            }
            if(event.getClickedBlock() != null) {
                if(event.getClickedBlock().getType() != Material.LECTERN) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
