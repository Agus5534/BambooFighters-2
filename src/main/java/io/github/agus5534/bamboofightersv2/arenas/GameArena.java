package io.github.agus5534.bamboofightersv2.arenas;

import io.github.agus5534.bamboofightersv2.utils.item.SquaredRegion;
import io.github.agus5534.bamboofightersv2.utils.location.Region;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;

@SuppressWarnings("ConstantConditions")
public class GameArena {
    private final Component arenaName;
    private final Region team1Region, team2Region;
    private final Location centerLoc;
    private final Material arenaIcon;
    private final SquaredRegion squaredRegion;

    GameArena(Region team1Region, Region team2Region, Location centerLoc, SquaredRegion squaredRegion, Component arenaName, Material arenaIcon) {
        this.team1Region = team1Region;
        this.team2Region = team2Region;
        this.centerLoc = centerLoc;
        this.squaredRegion = squaredRegion;
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

    public SquaredRegion getSquaredRegion() {
        return squaredRegion;
    }

    public Material getArenaIcon() {
        return arenaIcon;
    }
    public Component getArenaName() {
        return arenaName;
    }
}
