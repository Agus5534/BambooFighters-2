package io.github.agus5534.bamboofightersv2.arenas;

import com.google.gson.JsonElement;
import io.github.agus5534.bamboofightersv2.exceptions.GameArenaCreationException;
import io.github.agus5534.bamboofightersv2.utils.files.FileManager;
import io.github.agus5534.bamboofightersv2.utils.files.utils.JsonFile;
import io.github.agus5534.bamboofightersv2.utils.item.SquaredRegion;
import io.github.agus5534.bamboofightersv2.utils.location.LocationUtil;
import io.github.agus5534.bamboofightersv2.utils.location.Region;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ConstantConditions")
public class GameArenaManager {
    public static Set<GameArena> arenas;

    JsonFile json = FileManager.Arenas;

    public static GameArena lobby;

    public GameArenaManager() {
        arenas = new HashSet<>();
    }

    public void reloadArenas() {
        arenas.clear();
        GameArenaManager.lobby = null;


        json.getArray().forEach(this::loadArena);

        if(GameArenaManager.lobby == null) {
            throw new GameArenaCreationException("Missing Lobby Arena", new Throwable("Missing Lobby Arena"));
        }
    }

    private void loadArena(JsonElement element) {
        var object = element.getAsJsonObject();

        String name = (String) this.notNull(object.get("name").getAsString(), "Missing name key");
        boolean isTranslation = (Boolean) this.notNull(object.get("name-is-translation").getAsBoolean(), "Missing name-is-translation key");

        Component arenaName = isTranslation ? TranslatableText.basicTranslate(name) : Component.text(ChatColor.translateAlternateColorCodes('&', name));

        Location center = LocationUtil.of((String) this.notNull(object.get("center").getAsString(), "Missing center key"));
        Material material = Material.valueOf((String) this.notNull(object.get("material").getAsString(), "Missing material key"));

        var regionsObject = object.getAsJsonObject("regions");

        SquaredRegion squaredRegion = new SquaredRegion(
                LocationUtil.of((String) this.notNull(regionsObject.get("first-corner").getAsString(), "Missing first-corner key")),
                LocationUtil.of((String) this.notNull(regionsObject.get("second-corner").getAsString(), "Missing second-corner key"))
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
                material
        );

        if(name.equalsIgnoreCase("lobby")) {
            GameArenaManager.lobby = arena;
        } else {
            arenas.add(
                    arena
            );
        }

        Bukkit.getLogger().info("Registered arena " + name);
    }

    public GameArena notNull(GameArena o, String error) {
        if(o == null) {
            throw new GameArenaCreationException(error, new Throwable(error));
        }

        return o;
    }

    public Object notNull(Object o, String error) {
        if(o == null) {
            throw new GameArenaCreationException(error, new Throwable(error));
        }

        return o;
    }
}
