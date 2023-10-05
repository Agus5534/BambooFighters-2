package io.github.agus5534.bamboofightersv2.arenas;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.exceptions.GameArenaCreationException;
import io.github.agus5534.bamboofightersv2.utils.files.FileCreator;
import io.github.agus5534.bamboofightersv2.utils.files.FileManager;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ConstantConditions")
public class GameArenaManager {
    public static Set<GameArena> arenas;
    public static Set<GameArena> uncompleteArenas;
    FileCreator arenasDir = FileManager.ArenasDir;

    public static GameArena lobby;

    public GameArenaManager() {
        arenas = new HashSet<>();
    }

    public void reloadArenas() {
        uncompleteArenas.clear();
        arenas.clear();
        GameArenaManager.lobby = null;

        var arenaReader = BambooFighters.instance.arenaReader();

        arenaReader.getArenas().forEach(a -> {
            if(a.getArenaIcon() == null || a.getArenaName() == null || a.getCenterLoc() == null || a.getTeam1Region() == null || a.getTeam2Region() == null || a.getSquaredRegion() == null) {
                uncompleteArenas.add(a);
            }

            arenas.add(a);
        });

        if(GameArenaManager.lobby == null) {
            throw new GameArenaCreationException("Missing Lobby Arena", new Throwable("Missing Lobby Arena (It must be called 'lobby')"));
        }
    }

}
