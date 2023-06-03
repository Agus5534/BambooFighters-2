package io.github.agus5534.bamboofightersv2;

import io.github.agus5534.bamboofightersv2.arenas.ArenaReader;
import io.github.agus5534.bamboofightersv2.arenas.GameArenaManager;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.classes.list.*;
import io.github.agus5534.bamboofightersv2.commands.manager.CommandManager;
import io.github.agus5534.bamboofightersv2.game.GameCombat;
import io.github.agus5534.bamboofightersv2.listeners.ExtraListener;
import io.github.agus5534.bamboofightersv2.listeners.block.BlockListener;
import io.github.agus5534.bamboofightersv2.listeners.entity.EntityDamageListener;
import io.github.agus5534.bamboofightersv2.listeners.player.PlayerGameBasicsListener;
import io.github.agus5534.bamboofightersv2.listeners.player.PlayerInteractListener;
import io.github.agus5534.bamboofightersv2.listeners.player.PlayerJoinListener;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import io.github.agus5534.bamboofightersv2.utils.extra.ResourcePackUpdateChecker;
import io.github.agus5534.bamboofightersv2.utils.files.FileManager;
import io.github.agus5534.bamboofightersv2.utils.item.InteractionManager;
import io.github.agus5534.utils.scoreboard.MainScoreboard;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.gui.menu.listener.InventoryClickListener;
import team.unnamed.gui.menu.listener.InventoryOpenListener;
import team.unnamed.gui.menu.v1_19_R1.MenuInventoryWrapperImpl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class BambooFighters extends JavaPlugin {

    public static BambooFighters instance;
    public List<GameClass> gameClasses = new ArrayList<>();

    public static List<GameClass> staticGameClass = new ArrayList<>();

    public static HashMap<Player, GameTeam> playerGameTeamHashMap;

    private static GameCombat actualGameCombat = null;

    private static List<GameTeam> gameTeams;

    public List<Player> onAnimationPlayer = new ArrayList<>();

    public static List<String> convertedLocations = new ArrayList<>();

    private MenuInventoryWrapperImpl MenuInventoryWrapperImpl;
    private ArenaReader arenaReader;

    public static final Component tabHeader = TranslatableText.basicTranslate("game.player_list_title");

    private List<NamedTextColor> colors;

    private static List<String> savedTeamDates;
    @Override
    public void onEnable() {
        instance = this;
        arenaReader = new ArenaReader();

        playerGameTeamHashMap = new HashMap<>();
        gameTeams = new ArrayList<>();
        savedTeamDates = new ArrayList<>();

        new MainScoreboard();

        registerListeners(
                new InventoryClickListener(),
                new InventoryOpenListener(),
                new ExtraListener(this),
                new PlayerGameBasicsListener(),
                new EntityDamageListener(),
                new BlockListener(),
                new PlayerJoinListener(),
                new InteractionManager(),
                new PlayerInteractListener()
        );

        registerClasses(
                new VanguardiaClass(),
                new CazadorClass(),
                new MercenarioClass(),
                new LunariClass(),
                new MedicoClass()
        );

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ()-> {
            if(actualGameCombat != null) {
                if(!actualGameCombat.isStarted()) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(TranslatableText.basicTranslate("game.suggest.selectclass_command")));
                }
            }
        }, 10L, 1L);

        Arrays.stream(PlayerSelection.SelectionTier.values()).forEach(s -> {
            MainScoreboard.registerObjectiveDummy(s.name().replaceAll("_", ""));
        });

        new CommandManager().load();

        colors = new ArrayList<>();

        NamedTextColor[] textColors = {
                NamedTextColor.AQUA,
                NamedTextColor.BLUE,
                NamedTextColor.DARK_AQUA,
                NamedTextColor.DARK_BLUE,
                NamedTextColor.DARK_GRAY,
                NamedTextColor.DARK_RED,
                NamedTextColor.YELLOW,
                NamedTextColor.LIGHT_PURPLE,
                NamedTextColor.RED,
                NamedTextColor.GREEN,
                NamedTextColor.GRAY,
        };

        Arrays.asList(textColors).forEach(namedTextColor -> colors.add(namedTextColor));
        ResourcePackUpdateChecker resourcePackUpdateChecker = new ResourcePackUpdateChecker();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ()-> {
            try {
                resourcePackUpdateChecker.update();
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }, 300L, 12000L);

        GameArenaManager gameArenaManager = new GameArenaManager();
        gameArenaManager.reloadArenas();

        try {
            savedTeamDates = FileManager.loadObjectFile("savedTeamDates.bin");
        } catch (Exception e) {}
    }

    @Override
    public void onDisable() {
        FileManager.saveAsFile(savedTeamDates, "savedTeamDates.bin");
    }



    private void registerListeners(Listener... listeners) {
        for(var l : listeners) {
            Bukkit.getPluginManager().registerEvents(l, this);
        }
    }

    private void registerClasses(GameClass... gameClasses) {
        for(var g : gameClasses) {
            this.gameClasses.add(g);
            staticGameClass.add(g);
        }
    }

    public static GameCombat getActualGameCombat() {
        return actualGameCombat;
    }
    public static void setActualGameCombat(GameCombat actualGameCombat) {
        BambooFighters.actualGameCombat = actualGameCombat;
    }

    public static List<GameTeam> getGameTeams() {
        return gameTeams;
    }

    public List<GameClass> getGameClasses() {
        return gameClasses;
    }

    public static void addGameTeam(GameTeam gameTeam) {
        gameTeams.add(gameTeam);
    }

    public List<NamedTextColor> getColors() {
        return colors;
    }

    public String saveTeams() {
        var date = Calendar.getInstance().getTime();
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");

        savedTeamDates.add(dateFormat.format(date));

        FileManager.saveAsFile(gameTeams, String.format("teams/%s/teams.bin", dateFormat.format(date)));
        FileManager.saveAsFile(playerGameTeamHashMap, String.format("teams/%s/player_teams.bin", dateFormat.format(date)));

        return dateFormat.format(date);
    }

    public void loadTeams(String savedDate) throws Exception {
        gameTeams = FileManager.loadObjectFile(String.format("teams/%s/teams.bin", savedDate));
        playerGameTeamHashMap = FileManager.loadObjectFile(String.format("teams/%s/player_teams.bin", savedDate));
    }

    public ArenaReader arenaReader() {
        return arenaReader;
    }
}
