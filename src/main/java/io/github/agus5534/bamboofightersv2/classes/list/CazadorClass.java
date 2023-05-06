package io.github.agus5534.bamboofightersv2.classes.list;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.utils.item.ItemBuilder;
import io.github.agus5534.utils.items.ItemCreator;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class CazadorClass extends GameClass {

    private final HashMap<Integer, PotionEffect> integerPotionEffectHashMap;
    public CazadorClass() {
        super(BambooFighters.instance, "Cazador", Material.DIAMOND_SWORD);
        integerPotionEffectHashMap = new HashMap<>();
    }


    @Override
    protected void setItems(JavaPlugin plugin) {
        Component HELMET = TranslatableText.basicTranslate("class.cazador.item_helmet");
        Component CHESTPLATE = TranslatableText.basicTranslate("class.cazador.item_chestplate");
        Component LEGGINGS = TranslatableText.basicTranslate("class.cazador.item_leggings");
        Component BOOTS = TranslatableText.basicTranslate("class.cazador.item_boots");
        Component WEAPON = TranslatableText.basicTranslate("class.cazador.item_weapon");
        Component BOW = TranslatableText.basicTranslate("class.cazador.item_bow");
        Component ULTIMATE = TranslatableText.basicTranslate("class.cazador.item_ultimate");

        var classItems = this.getClassItems();

        classItems.put(39, new ItemCreator(Material.IRON_HELMET).name(HELMET).enchants(Enchantment.PROTECTION_PROJECTILE,1).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,1).setUnbreakable(true));
        classItems.put(38, new ItemCreator(Material.CHAINMAIL_CHESTPLATE).name(CHESTPLATE).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItems.put(37, new ItemCreator(Material.CHAINMAIL_LEGGINGS).name(LEGGINGS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setUnbreakable(true));
        classItems.put(36, new ItemCreator(Material.LEATHER_BOOTS).name(BOOTS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchants(Enchantment.PROTECTION_PROJECTILE, 1).setUnbreakable(true));

        classItems.put(0, new ItemCreator(Material.WOODEN_SWORD).name(WEAPON).setUnbreakable(true));
        classItems.put(2, new ItemBuilder(Material.BONE).setDisplayName(ULTIMATE).onConsumeRightClick("cazador_ultimate", event -> {
            var combat = BambooFighters.getActualGameCombat();
            if(combat == null) { return; }

            if(combat.hasUsedUltimate(event.getPlayer())) {
                event.getPlayer().sendMessage(TranslatableText.basicTranslate("error.ultimate.already_used"));
                return;
            }

            combat.playerJustUsedUltimate(event.getPlayer());

            int i = 0;

            while (i < 4) {
                var pl = event.getPlayer();
                var n = new Random().nextInt(999);

                var randomEffect = Arrays.stream(PotionEffectType.values()).toList().get(new Random().nextInt(Arrays.stream(PotionEffectType.values()).toList().size()));
                var potEff = new PotionEffect(randomEffect, 60, 0);
                integerPotionEffectHashMap.put(n, potEff);

                var arrowName = TranslatableText.basicTranslate("class.cazador.item_arrow", randomEffect.getName().toUpperCase());
                var item = new ItemCreator(Material.TIPPED_ARROW).name(arrowName).setCustomModelData(n);
                var arrowMeta = (PotionMeta) item.getItemMeta();
                arrowMeta.setColor(randomEffect.getColor());

                item.setItemMeta(arrowMeta);

                pl.getInventory().addItem(item);

                i++;
            }
        }).build());
        classItems.put(1, new ItemCreator(Material.BOW).name(BOW).enchants(Enchantment.ARROW_DAMAGE, 1).setUnbreakable(true));
        classItems.put(4, new ItemCreator(Material.ARROW).amount(16));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            var arrow = (Arrow)event.getDamager();
            var player = (Player)event.getEntity();
            if(arrow.getItemStack() != null) {
                var is = new ItemCreator(arrow.getItemStack());
                if(is.getItemMeta() == null) { return; }
                if(!is.getItemMeta().hasCustomModelData()) { return; }

                var effect = integerPotionEffectHashMap.get(is.getItemMeta().getCustomModelData());

                if(effect == null) { return; }

                player.addPotionEffect(effect);

            }
        }
    }
}
