package io.github.agus5534.bamboofightersv2.arenas;

import io.github.agus5534.bamboofightersv2.utils.LocationUtil;
import io.github.agus5534.bamboofightersv2.utils.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public enum GameArena {
    LOBBY(
            new Region(
                    LocationUtil.of("", "")
            ),
            new Region(
                    LocationUtil.of("", "")
            ),
            LocationUtil.of(""),
            "LOBBY",
            Material.STRUCTURE_VOID
    );

    private final String arenaName;
    private final Region team1Region, team2Region;
    private final Location centerLoc;
    private final Material arenaIcon;

    GameArena(Region team1Region, Region team2Region, Location centerLoc, String arenaName, Material arenaIcon) {
        this.team1Region = team1Region;
        this.team2Region = team2Region;
        this.centerLoc = centerLoc;
        this.arenaName = arenaName;
        this.arenaIcon = arenaIcon;
    }

    public Region getTeam1Region() {
        return team1Region;
    }

    public Region getTeam2Region() {
        return team2Region;
    }

    public Location getCenterLoc() {
        return centerLoc;
    }

    public Material getArenaIcon() {
        return arenaIcon;
    }
    public String getArenaName() {
        return arenaName;
    }
}
