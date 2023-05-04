package io.github.agus5534.bamboofightersv2.classes.list;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.utils.item.ItemBuilder;
import io.github.agus5534.utils.items.ItemCreator;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MercenarioClass extends GameClass {
    public MercenarioClass() {
        super(BambooFighters.instance, "Mercenario", Material.EMERALD);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(BambooFighters.instance, ()-> BambooFighters.playerGameTeamHashMap.values().forEach(g -> g.getPlayerGameClassHashMap().forEach((player, gameClass) -> {
            if(gameClass instanceof MercenarioClass && player.isOnline()) {
                if(BambooFighters.getActualGameCombat() == null) { return; }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 106, 0, false, false, false));
            }
        })), 20L, 5L);
    }


    @Override
    protected void setItems(JavaPlugin plugin) {
        Component HELMET = TranslatableText.basicTranslate("class.mercenario.item_helmet");
        Component CHESTPLATE = TranslatableText.basicTranslate("class.mercenario.item_chestplate");
        Component LEGGINGS = TranslatableText.basicTranslate("class.mercenario.item_leggings");
        Component BOOTS = TranslatableText.basicTranslate("class.mercenario.item_boots");
        Component WEAPON = TranslatableText.basicTranslate("class.mercenario.item_weapon");
        Component ULTIMATE = TranslatableText.basicTranslate("class.mercenario.item_ultimate");
        var classItem = this.getClassItems();

        classItem.put(39, new ItemCreator(Material.LEATHER_HELMET).name(HELMET).setUnbreakable(true));
        classItem.put(38, new ItemCreator(Material.CHAINMAIL_CHESTPLATE).name(CHESTPLATE).setUnbreakable(true).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
        classItem.put(37, new ItemCreator(Material.GOLDEN_LEGGINGS).name(LEGGINGS).setUnbreakable(true).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
        classItem.put(36, new ItemCreator(Material.GOLDEN_BOOTS).name(BOOTS).setUnbreakable(true).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchants(Enchantment.PROTECTION_PROJECTILE, 1));

        classItem.put(0, new ItemCreator(Material.STONE_SWORD).name(WEAPON).setUnbreakable(true).enchants(Enchantment.DAMAGE_ALL, 1));
        classItem.put(2, new ItemBuilder(Material.CARROT_ON_A_STICK).setDisplayName(ULTIMATE).onConsumeRightClick("mercenario_ultimate", event -> {
            var combat = BambooFighters.getActualGameCombat();
            if(combat == null) { return; }

            if(combat.hasUsedUltimate(event.getPlayer())) {
                event.getPlayer().sendMessage(TranslatableText.basicTranslate("error.ultimate.already_used"));
                return;
            }

            combat.playerJustUsedUltimate(event.getPlayer());

            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0));
        }).build());
    }
}
