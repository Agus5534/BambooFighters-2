package io.github.agus5534.bamboofightersv2.utils.files;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.utils.files.utils.JsonFile;

import java.io.*;

public class FileManager {

    public static FileCreator ArenasDir = new FileCreator(BambooFighters.instance.getDataFolder(), "/arenas/");
    public static JsonFile Combat = new JsonFile(getFile("combat.json"));
    public static JsonFile Config = new JsonFile(getFile("config.json"));


    private static File getFile(String name) {
        File f = new File(BambooFighters.instance.getDataFolder(), File.separator + name);

        if(!f.exists()) {
            BambooFighters.instance.saveResource(name, true);
        }

        return f;
    }

    private static File getFileNotRes(String name) {
        File f = new File(BambooFighters.instance.getDataFolder(), File.separator + name);

        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return f;
    }

    public static <T extends Object> void saveAsFile(T object, String name) {
        var f = getFileNotRes(name);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T extends Object> T loadObjectFile(String fileName) throws Exception {
        var f = getFileNotRes(fileName);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        T result = (T)ois.readObject();
        ois.close();

        return result;
    }
}
