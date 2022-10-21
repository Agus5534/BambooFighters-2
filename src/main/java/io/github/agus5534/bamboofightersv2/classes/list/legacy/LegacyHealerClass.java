package io.github.agus5534.bamboofightersv2.classes.list.legacy;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class LegacyHealerClass extends GameClass {

    private final BambooFighters plugin;

    public LegacyHealerClass(BambooFighters plugin) {
        super(plugin, "Legacy Healer", Material.GOLDEN_APPLE);
        this.plugin = plugin;
    }

    @Override
    protected void setItems(JavaPlugin plugin) {
        var hMap = this.classItems;

        hMap.put(103, new ItemCreator(Material.LEATHER_HELMET).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,1));
        hMap.put(102, new ItemCreator(Material.CHAINMAIL_CHESTPLATE).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,1));
        hMap.put(101, new ItemCreator(Material.LEATHER_LEGGINGS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,1));
        hMap.put(100, new ItemCreator(Material.IRON_BOOTS).enchants(Enchantment.PROTECTION_ENVIRONMENTAL,1));
        hMap.put(0, new ItemCreator(Material.WOODEN_SWORD));
        hMap.put(1, new ItemCreator(Material.GOLDEN_APPLE));
        hMap.put(2, new ItemCreator(Material.SPLASH_POTION).amount(3).potionEffect(PotionEffectType.HEAL,1,1));
        hMap.put(3, new ItemCreator(Material.SHEARS));
        hMap.put(4,new ItemCreator(Material.WHITE_WOOL).amount(16));
    }
}
