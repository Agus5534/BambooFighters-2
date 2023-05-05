package io.github.agus5534.bamboofightersv2.utils.files;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.utils.files.utils.JsonFile;
import org.bukkit.Bukkit;

import java.io.File;

public class FileManager {

    public static JsonFile Arenas = new JsonFile(getFile("arenas.json"));
    public static JsonFile Combat = new JsonFile(getFile("combat.json"));
    public static JsonFile Config = new JsonFile(getFile("config.json"));


    private static File getFile(String name) {
        File f = new File(BambooFighters.instance.getDataFolder(), File.separator + name);

        if(!f.exists()) {
            BambooFighters.instance.saveResource(name, true);
        }

        return f;
    }
}
