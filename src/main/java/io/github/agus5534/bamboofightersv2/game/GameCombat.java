package io.github.agus5534.bamboofightersv2.game;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.arenas.GameArena;
import io.github.agus5534.bamboofightersv2.arenas.GameArenaManager;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.utils.text.ComponentManager;
import io.github.agus5534.utils.text.MiniColor;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("all")
public class GameCombat {

    private final JavaPlugin plugin;
    private final List<GameArena> gameArenas;
    private final GameTeam team1;
    private final GameTeam team2;
    private int team1Score, team2Score, mainTask, countdownTask, timerMins, timerSecs;
    private final WorldBorder worldBorder;
    private long ticks = 6000;
    private GameArena lobby;
    private List<Player> ultimateUsed;
    private GameArena currentArena;
    private boolean started;

    private Component combatTabFooter;

    public GameCombat(JavaPlugin plugin, List<GameArena> gameArenas, GameTeam team1, GameTeam team2) {
        this.plugin = plugin;
        this.gameArenas = gameArenas;
        this.team1 = team1;
        this.team2 = team2;
        this.ultimateUsed = new ArrayList<>();
        this.worldBorder = Bukkit.getWorlds().get(0).getWorldBorder();
        this.started = false;

        team1Score = 0;
        team2Score = 0;

        Collections.shuffle(this.gameArenas);
        this.currentArena = this.gameArenas.stream().findFirst().get();
        this.gameArenas.remove(currentArena);

        this.configureWorldBorder();

        Bukkit.getScheduler().runTask(plugin, ()-> preStartCombat());

        Bukkit.getScheduler().runTaskLater(plugin, ()-> startCombat(), 600L);

        combatTabFooter = ComponentManager.formatMiniMessage(String.format("<bold>%s</bold> <yellow>%d</yellow>-<yellow>%d</yellow> <bold>%s</bold>",
                team1.getName(),
                team1Score,
                team2Score,
                team2.getName()));

        lobby = GameArenaManager.lobby;
        this.updateTabFooter();
    }

    public void incrementTeam1Score() { ++this.team1Score; }
    public void incrementTeam2Score() { ++this.team2Score; }

    private void startCombat() {
        this.started = true;
        this.ultimateUsed.clear();
        ticks = 6000;
        timerMins = 0;
        timerSecs = 0;
        teleportPlayers();
        this.configureWorldBorder();

        team1.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 10, false, false, false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 40, 100));
        });
        team2.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> {
            p.setGameMode(GameMode.SURVIVAL);
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 10, false, false, false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 40, 100));
        });

        team1.giveClasses();
        team2.giveClasses();

        Bukkit.broadcast(TranslatableText.basicTranslate("game.combat_starting",team1.getName(),team2.getName()));
        mainTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->gameTask(),1L,1L);
        this.currentArena.getTeam1Region().getRandomLocation().getWorld().setPVP(true);
    }

    private void preStartCombat() {
        this.started = false;
        teleportPlayers();
        this.configureWorldBorder();
        Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.pre_starting"));
        this.currentArena.getTeam1Region().getRandomLocation().getWorld().setPVP(false);
        this.currentArena.getTeam1Region().getRandomLocation().getWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);
        Bukkit.getOnlinePlayers().forEach(p -> p.setGameMode(GameMode.SPECTATOR));
    }

    private void gameTask() {
        if(ticks > 0) {
            formatTimer();
        }

        if(ticks == 600) {
            worldBorder.setSize(20, TimeUnit.SECONDS, 30);
        }

        if(getAlive(team1) == 0) {

            Bukkit.getScheduler().cancelTask(mainTask);

            incrementTeam2Score();

            updateTabFooter();

            currentArena.getSquaredRegion().getBlocksTypeOf(Material.WHITE_WOOL).forEach(b -> b.setType(Material.AIR));

            if(team2Score < 2) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.round_winner",team2.getName(), String.valueOf(team1Score+team2Score+1)));
                this.currentArena = gameArenas.get(ThreadLocalRandom.current().nextInt(gameArenas.size()));
                this.gameArenas.remove(currentArena);
                Bukkit.getScheduler().runTaskLater(plugin, ()-> preStartCombat(),200L);
                Bukkit.getScheduler().runTaskLater(plugin, ()-> startCombat(), 800L);
            } else {
                endGame(team2, team1);
            }

        }

        if(getAlive(team2) == 0) {

            Bukkit.getScheduler().cancelTask(mainTask);

            incrementTeam1Score();

            updateTabFooter();

            currentArena.getSquaredRegion().getBlocksTypeOf(Material.WHITE_WOOL).forEach(b -> b.setType(Material.AIR));

            if(team1Score < 2) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.round_winner",team1.getName(), String.valueOf(team1Score+team2Score+1)));
                this.currentArena = gameArenas.get(ThreadLocalRandom.current().nextInt(gameArenas.size()));
                this.gameArenas.remove(currentArena);
                Bukkit.getScheduler().runTaskLater(plugin, ()-> preStartCombat(),200L);
                Bukkit.getScheduler().runTaskLater(plugin, ()-> startCombat(), 800L);
            } else {
                endGame(team1, team2);
            }

        }
    }

    private void teleportPlayers() {
        team1.getMembers().stream().filter(p -> Bukkit.getOnlinePlayers().contains(p)).forEach(p -> p.teleport(this.currentArena.getTeam1Region().getRandomLocation()));
        team2.getMembers().stream().filter(p -> Bukkit.getOnlinePlayers().contains(p)).forEach(p -> p.teleport(this.currentArena.getTeam2Region().getRandomLocation()));

        Bukkit.getOnlinePlayers().forEach(p -> {
            var t = BambooFighters.playerGameTeamHashMap.get(p);
            if(t == null) { return; }

            if(t.equals(team1) || t.equals(team2)) { return; }

            p.teleport(this.currentArena.getCenterLoc());
        });
    }

    private int getAlive(GameTeam gameTeam) {
        return gameTeam.getMembers().stream()
                .filter(p -> p.isOnline())
                .filter(p -> p.getGameMode() == GameMode.SURVIVAL)
                .toList().size();
    }

    private void endGame(GameTeam winner, GameTeam loser) {
        combatTabFooter = Component.text("");
        updateTabFooter();
        Bukkit.broadcast(TranslatableText.basicTranslate("game.combat_winner",winner.getName()));

        winner.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.showTitle(Title.title(TranslatableText.basicTranslate("game.combat.title_win"), Component.text(""))));
        loser.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.showTitle(Title.title(TranslatableText.basicTranslate("game.combat.title_lose"), Component.text(""))));

        BambooFighters.setActualGameCombat(null);

        Bukkit.getScheduler().runTaskLater(plugin, ()->Bukkit.getOnlinePlayers().forEach(player -> {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 10, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 10));
            player.teleport(lobby.getCenterLoc());
        }),200L);

        this.currentArena = lobby;
        this.configureWorldBorder();
    }

    public void forceEnd() {
        combatTabFooter = Component.text("");
        updateTabFooter();
        Bukkit.broadcast(Component.text("Combat ended by force"));

        BambooFighters.setActualGameCombat(null);
        Bukkit.getWorlds().get(0).setPVP(false);

        Bukkit.getScheduler().runTaskLater(plugin, ()->Bukkit.getOnlinePlayers().forEach(player -> {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 10, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 10));
            player.teleport(lobby.getCenterLoc());
        }),200L);

        this.currentArena = lobby;
        this.configureWorldBorder();
    }

    private void formatTimer() {
        --ticks;
        long seconds = ticks / 20;

        timerMins = (int) seconds / 60;
        timerSecs = (int) seconds % 60;

        showTimer();
    }

    private void showTimer() {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(Component.text(ChatColor.GRAY + String.format("%02d:%02d", timerMins, timerSecs))));
    }

    public boolean hasUsedUltimate(Player player) {
        return this.ultimateUsed.contains(player);
    }

    public void playerJustUsedUltimate(Player player) {
        this.ultimateUsed.add(player);
    }

    private void updateTabFooter() {
        combatTabFooter = ComponentManager.formatMiniMessage(String.format("<bold>%s</bold> <yellow>%d</yellow>-<yellow>%d</yellow> <bold>%s</bold>",
                team1.getName(),
                team1Score,
                team2Score,
                team2.getName()));

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendPlayerListFooter(
                    combatTabFooter
            );
        });
    }

    private void configureWorldBorder() {
        worldBorder.setSize(500);
        worldBorder.setCenter(this.currentArena.getCenterLoc());
        worldBorder.setDamageAmount(0.0D);
    }

    public boolean isStarted() {
        return started;
    }

    public int getMainTask() {
        return mainTask;
    }
}
