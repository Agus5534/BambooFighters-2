package io.github.agus5534.bamboofightersv2.classes;

import io.github.agus5534.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public abstract class GameClass implements Listener {

    protected HashMap<Integer, ItemStack> classItems;

    protected final String className;
    protected final Material classMaterial;
    protected abstract void setItems(JavaPlugin plugin);

    public GameClass(JavaPlugin plugin, String className, Material classMaterial) {
        classItems = new HashMap<>();
        this.className = className;
        this.classMaterial = classMaterial;

        plugin.getServer().getPluginManager().registerEvents(this,plugin);

        classItems.put(6, new ItemCreator(Material.GOLDEN_APPLE));
        classItems.put(7, new ItemCreator(Material.SHEARS).setUnbreakable(true));
        classItems.put(8, new ItemCreator(Material.WHITE_WOOL).amount(16));

        setItems(plugin);
    }


    public Material getClassMaterial() {
        return classMaterial;
    }

    public String getClassName() {
        return className;
    }

    public HashMap<Integer, ItemStack> getClassItems() {
        return classItems;
    }
}
