package io.github.agus5534.bamboofightersv2.arenas;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.agus5534.bamboofightersv2.utils.files.FileCreator;
import io.github.agus5534.bamboofightersv2.utils.files.FileManager;
import io.github.agus5534.bamboofightersv2.utils.files.utils.JsonFile;
import io.github.agus5534.bamboofightersv2.utils.location.LocationUtil;
import io.github.agus5534.bamboofightersv2.utils.location.Region;
import io.github.agus5534.bamboofightersv2.utils.location.SquaredRegion;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ArenaReader {
    public List<GameArena> getArenas() {
        List<GameArena> gameArenas = new ArrayList<>();

        FileManager.ArenasDir.getContents(".json").forEach(f -> gameArenas.add(this.readArena(f)));

        return gameArenas;
    }

    public GameArena createArena(String fileName) {
        var file = new FileCreator(FileManager.ArenasDir.getFile(), fileName+".json");
        var arena = new GameArena(null, null, null, null, null, null, null,file.getFile());

        String jsonString = """
                {
                  "name": "",
                  "name-is-translation": false,
                  "center": "",
                  "material": "",
                  "regions": {
                    "first-corner": "",
                    "second-corner": "",
                    "first-team-spawn": [],
                    "second-team-spawn": []
                  }
                }
                """;

        try {
            FileWriter fw = new FileWriter(file.getFile(), false);

            fw.write(jsonString);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arena;
    }

    public void saveArena(GameArena arena) {
        var json = new JsonFile(arena.getFile());

        var name = PlainTextComponentSerializer.plainText().serialize(arena.getArenaName());

        json.setKey("name", name);
        json.getJsonObject().addProperty("name-is-translation", arena.isNameTranslation());
        json.setKey("center", LocationUtil.deserialize(arena.getCenterLoc()));
        json.setKey("material", arena.getArenaIcon().name());

        var regionsObject = new JsonObject();

        regionsObject.addProperty("first-corner", LocationUtil.deserialize(arena.getSquaredRegion().getFirstPoint()));
        regionsObject.addProperty("second-corner", LocationUtil.deserialize(arena.getSquaredRegion().getSecondPoint()));

        json.getJsonObject().add("regions", regionsObject);

        var firstTeamSpawnsArray = new JsonArray();
        var secondTeamSpawnsArray = new JsonArray();

        arena.getTeam1Region().getLocationList().forEach(l -> firstTeamSpawnsArray.add(LocationUtil.deserialize(l)));
        arena.getTeam2Region().getLocationList().forEach(l -> secondTeamSpawnsArray.add(LocationUtil.deserialize(l)));

        regionsObject.add("first-team-spawn", firstTeamSpawnsArray);
        regionsObject.add("second-team-spawn", secondTeamSpawnsArray);

        json.save();
    }

    public void deleteArena(GameArena arena) {
        arena.getFile().delete();
    }

    public GameArena getArena(String name) {
        return this.readArena(new FileCreator(FileManager.ArenasDir.getFile(), name+".json").getFile());
    }

    public GameArena readArena(File f) {
        var json = new JsonFile(f);
        var element = json.getJsonObject();
        var object = element.getAsJsonObject();

        String name = object.get("name").getAsString();
        boolean isTranslation = object.get("name-is-translation").getAsBoolean();

        Component arenaName = isTranslation ? TranslatableText.basicTranslate(name) : PlainTextComponentSerializer.plainText().deserialize(name);

        Location center = LocationUtil.of(object.get("center").getAsString());
        Material material = Material.valueOf(object.get("material").getAsString());

        var regionsObject = object.getAsJsonObject("regions");

        SquaredRegion squaredRegion = new SquaredRegion(
                LocationUtil.of(regionsObject.get("first-corner").getAsString()),
                LocationUtil.of(regionsObject.get("second-corner").getAsString())
        );

        List<Location> locsTeamOne = new ArrayList<>();
        regionsObject.getAsJsonArray("first-team-spawn").forEach(e -> locsTeamOne.add(LocationUtil.of(e.getAsString())));

        Region teamOneRegion = new Region(locsTeamOne);

        List<Location> locsTeamTwo = new ArrayList<>();

        regionsObject.getAsJsonArray("second-team-spawn").forEach(e -> locsTeamTwo.add(LocationUtil.of(e.getAsString())));

        Region teamTwoRegion = new Region(locsTeamTwo);

        var arena = new GameArena(
                teamOneRegion,
                teamTwoRegion,
                center,
                squaredRegion,
                arenaName,
                material,
                null,
                f
        );

        Bukkit.getLogger().info("Readed Arena " + name);

        return arena;
    }

}
