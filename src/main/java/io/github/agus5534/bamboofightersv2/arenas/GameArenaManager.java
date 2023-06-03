package io.github.agus5534.bamboofightersv2.arenas;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.utils.files.FileCreator;
import io.github.agus5534.bamboofightersv2.utils.files.FileManager;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ConstantConditions")
public class GameArenaManager {
    public static Set<GameArena> arenas;

    FileCreator arenasDir = FileManager.ArenasDir;

    public static GameArena lobby;

    public GameArenaManager() {
        arenas = new HashSet<>();
    }

    public void reloadArenas() {
        arenas.clear();
        GameArenaManager.lobby = null;

        var arenaReader = BambooFighters.instance.arenaReader();

        arenas.addAll(arenaReader.getArenas());

        if(GameArenaManager.lobby == null) {
           // throw new GameArenaCreationException("Missing Lobby Arena", new Throwable("Missing Lobby Arena"));
        }
    }

}
