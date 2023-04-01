package io.github.agus5534.bamboofightersv2.arenas;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public enum GameArena {
    LOBBY(
            new Location(Bukkit.getWorlds().get(0), -1, 31, 5),
            new Location(Bukkit.getWorlds().get(0), -1, 31, 5),
            new Location(Bukkit.getWorlds().get(0), -1, 31, 5),
            "LOBBY",
            Material.STRUCTURE_VOID
    );

    private final String arenaName;
    private final Location team1Loc, team2Loc, spectLoc;
    private final Material arenaIcon;

    GameArena(Location team1Loc, Location team2Loc, Location spectLoc, String arenaName, Material arenaIcon) {
        this.team1Loc = team1Loc;
        this.team2Loc = team2Loc;
        this.spectLoc = spectLoc;
        this.arenaName = arenaName;
        this.arenaIcon = arenaIcon;
    }

    public Location getTeam1Loc() {
        return team1Loc;
    }

    public Location getTeam2Loc() {
        return team2Loc;
    }

    public Location getSpectLoc() {
        return spectLoc;
    }

    public Material getArenaIcon() {
        return arenaIcon;
    }
    public String getArenaName() {
        return arenaName;
    }
}
