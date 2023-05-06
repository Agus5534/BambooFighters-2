package io.github.agus5534.bamboofightersv2.listeners.entity;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.utils.extra.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if(event.getEntity() instanceof Player) {
            event.getProjectile().setCustomName(event.getEntity().getName());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }

        var combat = BambooFighters.getActualGameCombat();

        if(Validate.isNull(combat)) { return; }

        var player = (Player) event.getEntity();

        if(player.getHealth() - event.getFinalDamage() > 0) { return; }

        event.setDamage(0);


        switch (event.getCause()) {
            case FIRE, FIRE_TICK, HOT_FLOOR, LAVA, POISON, WITHER, MAGIC, CUSTOM, SUICIDE, DROWNING, FALL -> combat.playerDied(player, event.getCause(), null);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }

        var player = (Player) event.getEntity();

        var combat = BambooFighters.getActualGameCombat();

        if(Validate.isNull(combat)) { return; }

        if(player.getHealth() - event.getFinalDamage() > 0) { return; }

        event.setCancelled(true);

        switch (event.getCause()) {
            case SUICIDE, POISON, WITHER, MAGIC, DROWNING, FALL, FIRE, FIRE_TICK, HOT_FLOOR, LAVA -> combat.playerDied(player, event.getCause(), null);
            case PROJECTILE, ENTITY_ATTACK, ENTITY_EXPLOSION, ENTITY_SWEEP_ATTACK -> combat.playerDied(player, event.getCause(), event.getDamager());
            default -> combat.playerDied(player, EntityDamageEvent.DamageCause.CUSTOM, null);
        }
    }

}
