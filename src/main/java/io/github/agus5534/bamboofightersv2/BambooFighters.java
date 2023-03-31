package io.github.agus5534.bamboofightersv2;

import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.classes.list.legacy.LegacyHealerClass;
import io.github.agus5534.bamboofightersv2.commands.ClassSelectorCommand;
import io.github.agus5534.bamboofightersv2.commands.CombatCommand;
import io.github.agus5534.bamboofightersv2.commands.CreateTeamCommand;
import io.github.agus5534.bamboofightersv2.commands.RestartServerCommand;
import io.github.agus5534.bamboofightersv2.game.GameCombat;
import io.github.agus5534.bamboofightersv2.listeners.ExtraListener;
import io.github.agus5534.bamboofightersv2.listeners.block.BlockListener;
import io.github.agus5534.bamboofightersv2.listeners.entity.EntityDamageListener;
import io.github.agus5534.bamboofightersv2.listeners.player.PlayerGameBasicsListener;
import io.github.agus5534.bamboofightersv2.listeners.player.PlayerInteractListener;
import io.github.agus5534.bamboofightersv2.listeners.player.PlayerJoinListener;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import io.github.agus5534.bamboofightersv2.utils.ResourcePackUpdateChecker;
import io.github.agus5534.bamboofightersv2.utils.item.InteractionManager;
import io.github.agus5534.utils.command.CommandRegisterer;
import io.github.agus5534.utils.scoreboard.MainScoreboard;
import io.github.agus5534.utils.text.ComponentManager;
import io.github.agus5534.utils.text.MiniColor;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.gui.menu.listener.InventoryClickListener;
import team.unnamed.gui.menu.listener.InventoryOpenListener;
import team.unnamed.gui.menu.v1_19_R1.MenuInventoryWrapperImpl;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class BambooFighters extends JavaPlugin {

    public static BambooFighters instance;
    public List<GameClass> gameClasses = new ArrayList<>();

    public static List<GameClass> staticGameClass = new ArrayList<>();

    public static HashMap<Player, GameTeam> playerGameTeamHashMap;

    private static GameCombat actualGameCombat = null;

    private static List<GameTeam> gameTeams;

    private CommandRegisterer commandRegisterer;

    private MenuInventoryWrapperImpl MenuInventoryWrapperImpl;

    public static final Component tabHeader = TranslatableText.basicTranslate("game.player_list_title");

    private List<NamedTextColor> colors;
    @Override
    public void onEnable() {
        instance = this;
        playerGameTeamHashMap = new HashMap<>();
        gameTeams = new ArrayList<>();
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

        registerClasses(new LegacyHealerClass(this));

        commandRegisterer = new CommandRegisterer(this);

        MainScoreboard.registerObjectivesDummy(PlayerSelection.SelectionTier.values().toString().replaceAll("_",""));

        try {
            commandRegisterer.setCommandConstructors(
                    new ClassSelectorCommand(this),
                    new CombatCommand(this),
                    new CreateTeamCommand(this),
                    new RestartServerCommand(this)
            );
        } catch (IllegalClassFormatException e) {
            throw new RuntimeException(e);
        }

        commandRegisterer.register();

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
        }, 12000L, 12000L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
}
