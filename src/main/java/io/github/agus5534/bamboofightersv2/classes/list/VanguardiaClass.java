package io.github.agus5534.bamboofightersv2.classes.list;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.utils.item.ItemBuilder;
import io.github.agus5534.utils.items.ItemCreator;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VanguardiaClass extends GameClass {
    public VanguardiaClass() {
        super(BambooFighters.instance, "Vanguardia", Material.IRON_CHESTPLATE);
    }
    private String HELMET = TranslatableText.basicTranslateString("class.vanguardia.item_helmet");
    private String CHESTPLATE = TranslatableText.basicTranslateString("class.vanguardia.item_chestplate");
    private String LEGGINGS = TranslatableText.basicTranslateString("class.vanguardia.item_leggings");
    private String BOOTS = TranslatableText.basicTranslateString("class.vanguardia.item_boots");
    private String WEAPON = TranslatableText.basicTranslateString("class.vanguardia.item_weapon");

    @Override
    protected void setItems(JavaPlugin plugin) {
        var classItem = this.getClassItems();

        classItem.put(39, new ItemCreator(Material.IRON_HELMET).name(HELMET).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchants(Enchantment.PROTECTION_PROJECTILE, 1).setUnbreakable(true));
        classItem.put(38, new ItemCreator(Material.IRON_CHESTPLATE).name(CHESTPLATE).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItem.put(37, new ItemCreator(Material.IRON_LEGGINGS).name(LEGGINGS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItem.put(36, new ItemCreator(Material.IRON_BOOTS).name(BOOTS).enchants(Enchantment.PROTECTION_PROJECTILE, 1).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItem.put(0, new ItemCreator(Material.WOODEN_SWORD).name(WEAPON).setUnbreakable(true));
        classItem.put(40, new ItemCreator(Material.SHIELD)); //TODO Shield Usages
        classItem.put(2, new ItemBuilder(Material.STICK).setDisplayName("Ultimate").onConsumeRightClick("vanguardia_ultimate", event -> {
            var combat = BambooFighters.getActualGameCombat();
            if(combat == null) { return; }

            if(combat.hasUsedUltimate(event.getPlayer())) { return; }

            combat.playerJustUsedUltimate(event.getPlayer());

            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
        }).build());
    }
}
