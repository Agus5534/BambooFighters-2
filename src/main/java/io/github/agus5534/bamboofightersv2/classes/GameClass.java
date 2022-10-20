package io.github.agus5534.bamboofightersv2.classes;

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
