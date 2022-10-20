package io.github.agus5534.bamboofightersv2.listeners;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.text.DecimalFormat;

public class ExtraListener implements Listener {
    BambooFighters plugin;

    public ExtraListener(BambooFighters plugin) { this.plugin = plugin; }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if(event.getHitEntity() == null) { return; }
        if(!(event.getHitEntity() instanceof Player)) { return; }
        if(event.getEntity().getType() != EntityType.ARROW) { return; }

        var player = (Player) event.getHitEntity();
        var thrower = Bukkit.getPlayer(event.getEntity().getCustomName());


        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            var phealth = player.getHealth();
            String vida = new DecimalFormat("##.##").format(phealth);

            if(phealth <= 19) {
                thrower.sendMessage(TranslatableText.basicTranslate("action.arrow_hit",player.getName(), vida));
            }
        },2L);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(BambooFighters.getActualGameCombat() == null) {
            event.setCancelled(true);
        }
    }
}
