package io.github.agus5534.bamboofightersv2.listeners.entity;

import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
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

        var player = (Player) event.getEntity();

        if(player.getHealth() - event.getFinalDamage() > 0) { return; }

        event.setDamage(0);

        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        switch (event.getCause()) {
            case FIRE, FIRE_TICK, HOT_FLOOR, LAVA -> broadcastDeathMessage("custom.death.cause_fire",player.getName());
            case FALL -> broadcastDeathMessage("custom.death.cause_fall",player.getName());
            case DROWNING -> broadcastDeathMessage("custom.death.cause_drowning",player.getName());
            case SUICIDE -> broadcastDeathMessage("custom.death.cause_suicide",player.getName());
            case POISON, WITHER, MAGIC -> broadcastDeathMessage("custom.death.cause_effect",player.getName());
            case CUSTOM -> broadcastDeathMessage("custom.death.cause_unknown",player.getName());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }

        var player = (Player) event.getEntity();

        if(player.getHealth() - event.getFinalDamage() > 0) { return; }

        event.setCancelled(true);

        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        switch (event.getCause()) {
            case SUICIDE -> broadcastDeathMessage("custom.death.cause_suicide",player.getName());
            case PROJECTILE -> broadcastDeathMessage("custom.death.cause_projectile",event.getDamager().getCustomName(),player.getName());
            case FIRE, FIRE_TICK, HOT_FLOOR, LAVA -> broadcastDeathMessage("custom.death.cause_fire",player.getName());
            case FALL -> broadcastDeathMessage("custom.death.cause_fall",player.getName());
            case DROWNING -> broadcastDeathMessage("custom.death.cause_drowning",player.getName());
            case POISON, WITHER, MAGIC -> broadcastDeathMessage("custom.death.cause_effect",player.getName());
            case ENTITY_ATTACK, ENTITY_EXPLOSION, ENTITY_SWEEP_ATTACK -> broadcastDeathMessage("custom.death.cause_player",event.getDamager().getName(),player.getName());
            default -> broadcastDeathMessage("custom.death.cause_unknown",player.getName());
        }
    }

    private void broadcastDeathMessage(String key, String... values) {
        Bukkit.broadcast(TranslatableText.basicTranslate(key, values));
    }

}
