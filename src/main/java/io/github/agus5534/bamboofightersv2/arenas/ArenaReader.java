package io.github.agus5534.bamboofightersv2.arenas;

import io.github.agus5534.bamboofightersv2.utils.files.FileCreator;
import io.github.agus5534.bamboofightersv2.utils.files.FileManager;
import io.github.agus5534.bamboofightersv2.utils.files.utils.JsonFile;
import io.github.agus5534.bamboofightersv2.utils.location.LocationUtil;
import io.github.agus5534.bamboofightersv2.utils.location.Region;
import io.github.agus5534.bamboofightersv2.utils.location.SquaredRegion;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ArenaReader {
    public List<GameArena> getArenas() {
        List<GameArena> gameArenas = new ArrayList<>();

        FileManager.ArenasDir.getContents(".json").forEach(f -> gameArenas.add(this.readArena(f)));

        return gameArenas;
    }

    public void saveArena(GameArena arena) {
        var json = new JsonFile(arena.getFileCreator().getFile());

        json.save();
    }

    public void deleteArena(GameArena arena) {
        arena.getFileCreator().getFile().delete();
    }

    public GameArena readArena(FileCreator f) {
        var json = new JsonFile(f.getFile());
        var element = json.getJsonObject();
        var object = element.getAsJsonObject();

        String name = object.get("name").getAsString();
        boolean isTranslation = object.get("name-is-translation").getAsBoolean();

        Component arenaName = isTranslation ? TranslatableText.basicTranslate(name) : Component.text(ChatColor.translateAlternateColorCodes('&', name));

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
                f
        );

        return arena;
    }

}
