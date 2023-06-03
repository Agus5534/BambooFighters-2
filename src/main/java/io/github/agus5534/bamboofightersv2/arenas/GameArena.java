package io.github.agus5534.bamboofightersv2.arenas;

import io.github.agus5534.bamboofightersv2.utils.location.Region;
import io.github.agus5534.bamboofightersv2.utils.location.SquaredRegion;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;

@SuppressWarnings("ConstantConditions")
public class GameArena {
    private Component arenaName;
    private Region team1Region, team2Region;
    private Location centerLoc;
    private Material arenaIcon;
    private SquaredRegion squaredRegion;
    private boolean nameTranslation;
    private final File file;

    public GameArena(Region team1Region, Region team2Region, Location centerLoc, SquaredRegion squaredRegion, Component arenaName, Material arenaIcon, File file) {
        this.team1Region = team1Region;
        this.team2Region = team2Region;
        this.centerLoc = centerLoc;
        this.squaredRegion = squaredRegion;
        this.arenaName = arenaName;
        this.arenaIcon = arenaIcon;
        this.file = file;
        this.nameTranslation = false;
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

    public SquaredRegion getSquaredRegion() {
        return squaredRegion;
    }

    public Material getArenaIcon() {
        return arenaIcon;
    }
    public Component getArenaName() {
        return arenaName;
    }

    public void setArenaName(Component arenaName) {
        this.arenaName = arenaName;
    }

    public void setTeam1Region(Region team1Region) {
        this.team1Region = team1Region;
    }

    public void setTeam2Region(Region team2Region) {
        this.team2Region = team2Region;
    }

    public void setCenterLoc(Location centerLoc) {
        this.centerLoc = centerLoc;
    }

    public void setArenaIcon(Material arenaIcon) {
        this.arenaIcon = arenaIcon;
    }

    public void setSquaredRegion(SquaredRegion squaredRegion) {
        this.squaredRegion = squaredRegion;
    }

    public boolean isNameTranslation() {
        return nameTranslation;
    }

    public File getFile() {
        return file;
    }
}
