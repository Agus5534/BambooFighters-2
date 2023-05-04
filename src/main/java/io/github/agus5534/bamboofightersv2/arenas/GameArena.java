package io.github.agus5534.bamboofightersv2.arenas;

import io.github.agus5534.bamboofightersv2.utils.location.LocationUtil;
import io.github.agus5534.bamboofightersv2.utils.location.Region;
import io.github.agus5534.bamboofightersv2.utils.item.SquaredRegion;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;

@SuppressWarnings("ConstantConditions")
public enum GameArena {
    LOBBY(
            new Region(LocationUtil.of("Panda,-1.68,31,-5.41,122.64,84.44", "Panda,-1.68,31,-5.41,122.64,84.44")),
            new Region(LocationUtil.of("Panda,-1.68,31,-5.41,122.64,84.44", "Panda,-1.68,31,-5.41,122.64,84.44")),
            LocationUtil.of("Panda,-1.68,31,-5.41,122.64,84.44"),
            new SquaredRegion(LocationUtil.of("Panda,-1.68,31,-5.41,122.64,84.44"), LocationUtil.of("Panda,-1.68,31,-5.41,122.64,84.44")),
            TranslatableText.basicTranslate("arena.lobby"),
            Material.HAY_BLOCK
    ),
    LUNA(
            new Region(LocationUtil.of(
                    "Panda,997.5,10,958.5,41.97,-16.2",
                        "Panda,994.48,10,957.56,38.07,-10.35",
                        "Panda,1000.6,10,962.21,52.02,-9.3"
            )),
            new Region(LocationUtil.of(
                    "Panda,907.09,10,1050.02,-140.88,-9.15",
                        "Panda,909.49,10,1050.59,-142.23,-9.15",
                        "Panda,905.67,10,1047.06,-136.83,-7.5"
            )),
            LocationUtil.of("Panda,952.38,28.5,1003.93,-178.38,8.7"),
            new SquaredRegion(LocationUtil.of("Panda,1001.49,64,1053.67,123.4,57.98"), LocationUtil.of("Panda,902.39,3,953.36,-32.63,37.52")),
            TranslatableText.basicTranslate("arena.luna"),
            Material.END_STONE
    ),
    JARDINES_MARCIANOS(
            new Region(LocationUtil.of(
                    "Panda,1079.64,21,1176.15,-37.01,22.09",
                        "Panda,1076.89,21,1178.22,-37.01,22.09",
                        "Panda,1074.34,21,1180.94,-37.01,22.09"
            )),
            new Region(LocationUtil.of(
                    "Panda,1168.22,21,1265.34,80,48.95",
                        "Panda,1165.73,21,1267.82,80,48.95",
                        "Panda,1163.16,21,1270.17,80,48.95"
            )),
            LocationUtil.of("Panda,1121.75,34.53,1223.21,179.12,-4.89"),
            new SquaredRegion(LocationUtil.of("Panda,1072,66,1173,-37,48"), LocationUtil.of("Panda,1172,111,1274,138,3")),
            TranslatableText.basicTranslate("arena.jardines_marcianos"),
            Material.MOSS_BLOCK
    ),
    TA_HELAO(
            new Region(LocationUtil.of(
                    "Panda,1077.27,24,1321.14,-79.74,10.38",
                        "Panda,1077,24,1322.62,-79.74,10.38",
                        "Panda,1077.22,24,1324.01,-79.74,10.38"
            )),
            new Region(LocationUtil.of(
                    "Panda,1163.67,24,1393.04,89.09,2.2",
                        "Panda,1163.64,24,1389.8,89.09,2.2",
                        "Panda,1163.65,24,1391.31,89.09,2.2"
            )),
            LocationUtil.of("Panda,1122.36,50.5,1360.86,177.7,84.46"),
            new SquaredRegion(LocationUtil.of("Panda,1170,14,1305,55,-36"), LocationUtil.of("Panda,1070,74,1407,-136,56")),
            TranslatableText.basicTranslate("arena.helao"),
            Material.BLUE_ICE
    ),
    FUNGICO_Y_FRONDOSO(
            new Region(LocationUtil.of(
                    "Panda,631.99,24,1139.68,-28.99,-8.87",
                        "Panda,633.11,24,1139.06,-28.99,-8.87",
                        "Panda,633.04,24,1137.13,-28.99,-8.87"
            )),
            new Region(LocationUtil.of(
                    "Panda,721.7,24,1220.6,149.99,20.82",
                        "Panda,720.77,24,1221.14,154.73,-17.51",
                        "Panda,719.97,24,1222.03,154.73,-17.51"
            )),
            LocationUtil.of("Panda,672.73,49.03,1182.54,-77.97,29.82"),
            new SquaredRegion(LocationUtil.of("Panda,726,18,1129,67,25"), LocationUtil.of("Panda,627,60,1229,-126,52")),
            TranslatableText.basicTranslate("arena.fungico_y_frondoso"),
            Material.MANGROVE_PROPAGULE
    ),
    NAVE_ENIGMATICA(
            new Region(LocationUtil.of(
                            "Panda,608.32,53,998.63,-179.35,16.84",
                                "Panda,607.25,53,998.63,179.7,2.28",
                                "Panda,605.74,53,998.63,179.7,2.28"
            )),
            new Region(LocationUtil.of(
                            "Panda,608.3,53,884.3,7.22,-7.89",
                                "Panda,607.12,53,882.73,-2,-14.4",
                                "Panda,605.3,53,884.3,-2,-14.4"
            )),
            LocationUtil.of("Panda,583.44,54,935.56,-90.5,-0.56"),
            new SquaredRegion(LocationUtil.of("Panda,559,82,1013,146,86"), LocationUtil.of("Panda,656,35,862,148,84")),
            TranslatableText.basicTranslate("arena.nave_enigmatica"),
            Material.IRON_BLOCK
    );

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
