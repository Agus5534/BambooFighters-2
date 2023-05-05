package io.github.agus5534.bamboofightersv2.utils.files.utils;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonFile {

    private final File file;
    private JsonElement element;

    public JsonFile(File file) {
        this.file = file;

        try {
            element = JsonParser.parseReader(new FileReader(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonObject getJsonObject() {
        return element.getAsJsonObject();
    }

    public JsonArray getArray() {
        return element.getAsJsonArray();
    }

    public JsonElement getKey(String key) {
        return getJsonObject().get(key);
    }

    public void setKey(String key, String value) {
        getJsonObject().addProperty(key, value);
    }

    public JsonElement getKey(int index) {
        return getArray().get(index);
    }

    public void save() {
        String s = new Gson().toJson(element);

        try {
            FileWriter fw = new FileWriter(file,false);

            fw.write(s);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reload() {
        try {
            element = JsonParser.parseReader(new FileReader(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
