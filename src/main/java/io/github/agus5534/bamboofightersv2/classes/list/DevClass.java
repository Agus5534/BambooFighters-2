package io.github.agus5534.bamboofightersv2.classes.list;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.utils.items.ItemCreator;
import io.github.agus5534.utils.items.ItemPersistentData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DevClass extends GameClass {
    private final BambooFighters plugin;

    public DevClass(BambooFighters plugin) {
        super(plugin,"development", Material.DEBUG_STICK);
        this.plugin = plugin;
    }

    @Override
    protected void setItems(JavaPlugin plugin) {
        var classItem = this.getClassItems();

        classItem.put(3,new ItemCreator(Material.POPPY).name(Component.text("DEV resurrección")));
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }

        var player = (Player) event.getEntity();

        if(player.getHealth() - event.getFinalDamage() > 0) { return; }

        var inv = player.getInventory();

        for(var i : inv.getContents()) {
            if(i == null) { continue; }
            if(!i.hasItemMeta()) { continue; }

            ItemPersistentData itemPersistentData = new ItemPersistentData(plugin, "data_dev", i.getItemMeta());

            if(itemPersistentData.hasData(PersistentDataType.STRING)) {
                if(itemPersistentData.getData(PersistentDataType.STRING).equals("si")) {
                    event.setDamage(0);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 2));
                    inv.remove(i);
                }
            }
        }

    }
}
