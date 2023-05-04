package io.github.agus5534.bamboofightersv2.classes.list;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.utils.item.ItemBuilder;
import io.github.agus5534.utils.items.ItemCreator;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class MedicoClass extends GameClass {
    public MedicoClass() {
        super(BambooFighters.instance, "Médico", Material.IRON_CHESTPLATE);
    }
    @Override
    protected void setItems(JavaPlugin plugin) {
        Component HELMET = TranslatableText.basicTranslate("class.medico.item_helmet");
        Component CHESTPLATE = TranslatableText.basicTranslate("class.medico.item_chestplate");
        Component LEGGINGS = TranslatableText.basicTranslate("class.medico.item_leggings");
        Component BOOTS = TranslatableText.basicTranslate("class.medico.item_boots");
        Component WEAPON = TranslatableText.basicTranslate("class.medico.item_weapon");
        Component ULTIMATE = TranslatableText.basicTranslate("class.medico.item_ultimate");
        Component BOW = TranslatableText.basicTranslate("class.medico.item_bow");
        var classItem = this.getClassItems();

        classItem.put(39, new ItemCreator(Material.LEATHER_HELMET).name(HELMET).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchants(Enchantment.PROTECTION_PROJECTILE, 1).setUnbreakable(true));
        classItem.put(38, new ItemCreator(Material.CHAINMAIL_CHESTPLATE).name(CHESTPLATE).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItem.put(37, new ItemCreator(Material.LEATHER_LEGGINGS).name(LEGGINGS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItem.put(36, new ItemCreator(Material.IRON_BOOTS).name(BOOTS).enchants(Enchantment.PROTECTION_PROJECTILE, 1).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItem.put(0, new ItemCreator(Material.IRON_HOE).name(WEAPON).enchants(Enchantment.KNOCKBACK, 1).setUnbreakable(true));
        var item = new ItemCreator(Material.SPLASH_POTION).name(ChatColor.translateAlternateColorCodes('&', "&cHealing Potion")).potionEffect(PotionEffectType.HEAL, 60, 1);
        var potionMeta = (PotionMeta) item.getItemMeta();
        potionMeta.setColor(PotionEffectType.HEAL.getColor());
        potionMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        item.setItemMeta(potionMeta);

        classItem.put(1, new ItemCreator(Material.BOW).name(BOW).enchants(Enchantment.ARROW_KNOCKBACK, 2).setUnbreakable(true));
        classItem.put(28, new ItemCreator(Material.ARROW).amount(6));
        classItem.put(3, item);
        classItem.put(4, item);
        classItem.put(5, item);
        classItem.put(2, new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayName(ULTIMATE).onConsumeRightClick("medico_ultimate", event -> {
            var combat = BambooFighters.getActualGameCombat();
            if(combat == null) { return; }

            if(combat.hasUsedUltimate(event.getPlayer())) {
                event.getPlayer().sendMessage(TranslatableText.basicTranslate("error.ultimate.already_used"));
                return;
            }

            var team = BambooFighters.playerGameTeamHashMap.get(event.getPlayer());
            if(team == null) { return; }

            combat.playerJustUsedUltimate(event.getPlayer());


            team.getMembers().forEach(p -> {
                if(p.isOnline()) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, true, true, true));
                }
            });
        }).build());
    }
}
