package io.github.agus5534.bamboofightersv2.classes.list;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.utils.item.ItemBuilder;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LunariClass extends GameClass {
    public LunariClass() {
        super(BambooFighters.instance, "Lunari", Material.LEATHER_HELMET);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BambooFighters.instance, ()-> BambooFighters.playerGameTeamHashMap.values().forEach(g -> g.getPlayerGameClassHashMap().forEach((player, gameClass) -> {
            if(gameClass instanceof LunariClass && player.isOnline()) {
                if(BambooFighters.getActualGameCombat() == null) { return; }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 106, 0, false, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 106, 0, false, false, false));
            }
        })), 20L, 5L);
    }
    private Component WEAPON = TranslatableText.basicTranslate("class.lunari.item_weapon");
    @Override
    protected void setItems(JavaPlugin plugin) {
        var classItem = this.getClassItems();

        classItem.put(39, new ItemBuilder(Material.LEATHER_HELMET).setDisplayName("Armadura Lunari").addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).setUnbreakable(true).build());
        classItem.put(38, new ItemBuilder(Material.LEATHER_CHESTPLATE).setDisplayName("Armadura Lunari").addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        classItem.put(37, new ItemBuilder(Material.LEATHER_LEGGINGS).setDisplayName("Armadura Lunari").addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        classItem.put(36, new ItemBuilder(Material.GOLDEN_BOOTS).setDisplayName("Botas Lunari").addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        classItem.put(0, new ItemBuilder(Material.GOLDEN_AXE).setDisplayName(WEAPON).setUnbreakable(true).build());
    }
}
