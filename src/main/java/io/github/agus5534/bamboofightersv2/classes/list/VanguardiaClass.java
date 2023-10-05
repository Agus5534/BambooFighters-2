package io.github.agus5534.bamboofightersv2.classes.list;

import io.github.agus5534.agusutils.utils.text.TranslatableText;
import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.utils.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VanguardiaClass extends GameClass {
    public VanguardiaClass() {
        super(BambooFighters.instance, "Vanguardia", Material.IRON_CHESTPLATE);
    }

    @Override
    protected void setItems(JavaPlugin plugin) {
        Component HELMET = TranslatableText.basicTranslate("class.vanguardia.item_helmet");
        Component CHESTPLATE = TranslatableText.basicTranslate("class.vanguardia.item_chestplate");
        Component LEGGINGS = TranslatableText.basicTranslate("class.vanguardia.item_leggings");
        Component BOOTS = TranslatableText.basicTranslate("class.vanguardia.item_boots");
        Component WEAPON = TranslatableText.basicTranslate("class.vanguardia.item_weapon");
        Component ULTIMATE = TranslatableText.basicTranslate("class.vanguardia.item_ultimate");
        Component SHIELD = TranslatableText.basicTranslate("class.vanguardia.item_shield");

        var classItem = this.getClassItems();

        classItem.put(39, new ItemBuilder(Material.IRON_HELMET).setDisplayName(HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).setUnbreakable(true).build());
        classItem.put(38, new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        classItem.put(37, new ItemBuilder(Material.IRON_LEGGINGS).setDisplayName(LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        classItem.put(36, new ItemBuilder(Material.IRON_BOOTS).setDisplayName(BOOTS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true).build());
        classItem.put(0, new ItemBuilder(Material.WOODEN_SWORD).setDisplayName(ULTIMATE).setUnbreakable(true).build());

        var shield = new ItemBuilder(Material.SHIELD).setDisplayName(SHIELD).build();
        var meta = (Damageable)shield.getItemMeta();
        meta.setDamage(292);
        shield.setItemMeta(meta);

        classItem.put(40, shield);

        classItem.put(2, new ItemBuilder(Material.BLAZE_ROD).setDisplayName(WEAPON).onConsumeRightClick("vanguardia_ultimate", event -> {
            var combat = BambooFighters.getActualGameCombat();
            if(combat == null) { return; }

            if(combat.hasUsedUltimate(event.getPlayer())) {
                event.getPlayer().sendMessage(TranslatableText.basicTranslate("error.ultimate.already_used"));
                return;
            }

            combat.playerJustUsedUltimate(event.getPlayer());

            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
        }).build());
    }
}
