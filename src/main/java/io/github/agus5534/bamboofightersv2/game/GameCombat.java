package io.github.agus5534.bamboofightersv2.game;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.arenas.GameArena;
import io.github.agus5534.bamboofightersv2.arenas.GameArenaManager;
import io.github.agus5534.bamboofightersv2.exceptions.GameCombatStartException;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.bamboofightersv2.utils.extra.TimeFormatter;
import io.github.agus5534.bamboofightersv2.utils.extra.Validate;
import io.github.agus5534.bamboofightersv2.utils.files.FileManager;
import io.github.agus5534.bamboofightersv2.utils.files.utils.JsonFile;
import io.github.agus5534.utils.text.ChatFormatter;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ConstantConditions")
public class GameCombat {

    private final JavaPlugin plugin;
    private final List<GameArena> gameArenas;
    private final GameTeam team1;
    private final GameTeam team2;
    private int team1Score, team2Score, mainTask, countdownTask, timerMins, timerSecs;
    private float scoreForWin;
    private WorldBorder worldBorder;
    private long ticks;
    private TimeFormatter worldBorderDelay;
    private List<Player> ultimateUsed;
    private GameArena currentArena;
    private boolean started;
    private JsonFile json;
    private Component combatTabFooter;

    public GameCombat(JavaPlugin plugin, List<GameArena> gameArenas, GameTeam team1, GameTeam team2) {
        this.plugin = plugin;
        this.gameArenas = gameArenas;
        this.team1 = team1;
        this.team2 = team2;
        this.ultimateUsed = new ArrayList<>();
        this.started = false;
        this.json = FileManager.Combat;

        this.ticks = (long) new TimeFormatter(Validate.notNull(json.getKey("combat-duration").getAsString(), "Missing combat-duration key", new GameCombatStartException())).convertTo(TimeFormatter.Format.TICKS);
        this.worldBorderDelay = new TimeFormatter(Validate.notNull(json.getKey("worldborder-delay").getAsString(), "Missing worldborder-delay key", new GameCombatStartException()));

        team1Score = 0;
        team2Score = 0;
        scoreForWin = (float) json.getKey("combat-rounds").getAsInt() / 2;

        Collections.shuffle(this.gameArenas);

        this.currentArena = this.gameArenas.stream().findFirst().get();
        this.worldBorder = currentArena.getCenterLoc().getWorld().getWorldBorder();

        this.gameArenas.remove(currentArena);

        this.configureWorldBorder();

        Bukkit.getScheduler().runTask(plugin, ()-> preStartCombat());

        Bukkit.getScheduler().runTaskLater(plugin, ()-> startCombat(), (long) new TimeFormatter(Validate.notNull(json.getKey("precombat-start-duration").getAsString(), "Missing precombat-start-duration key", new GameCombatStartException())).convertTo(TimeFormatter.Format.TICKS));

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

        if(ticks == (long) worldBorderDelay.convertTo(TimeFormatter.Format.TICKS)) {
            worldBorder.setSize(20, (long) worldBorderDelay.convertTo(TimeFormatter.Format.SECONDS));
        }

        if(getAlive(team1) == 0) {

            Bukkit.getScheduler().cancelTask(mainTask);

            incrementTeam2Score();

            updateTabFooter();

            currentArena.getSquaredRegion().getBlocksTypeOf(Material.WHITE_WOOL).forEach(b -> b.setType(Material.AIR));

            if(team2Score < scoreForWin) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.round_winner",team2.getName(), String.valueOf(team1Score+team2Score+1)));

                this.currentArena = gameArenas.get(ThreadLocalRandom.current().nextInt(gameArenas.size()));
                this.worldBorder = currentArena.getCenterLoc().getWorld().getWorldBorder();
                this.gameArenas.remove(currentArena);

                Bukkit.getScheduler().runTaskLater(plugin, ()-> preStartCombat(),100L);
                Bukkit.getScheduler().runTaskLater(plugin, ()-> startCombat(), (long) new TimeFormatter(Validate.notNull(json.getKey("precombat-start-duration").getAsString(), "Missing precombat-start-duration key", new GameCombatStartException())).convertTo(TimeFormatter.Format.TICKS) + 100L);
            } else {
                endGame(team2, team1);
            }

        }

        if(getAlive(team2) == 0) {

            Bukkit.getScheduler().cancelTask(mainTask);

            incrementTeam1Score();

            updateTabFooter();

            currentArena.getSquaredRegion().getBlocksTypeOf(Material.WHITE_WOOL).forEach(b -> b.setType(Material.AIR));

            if(team1Score < scoreForWin) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.round_winner",team1.getName(), String.valueOf(team1Score+team2Score+1)));

                this.currentArena = gameArenas.get(ThreadLocalRandom.current().nextInt(gameArenas.size()));
                this.gameArenas.remove(currentArena);
                this.worldBorder = currentArena.getCenterLoc().getWorld().getWorldBorder();

                Bukkit.getScheduler().runTaskLater(plugin, ()-> preStartCombat(),100L);
                Bukkit.getScheduler().runTaskLater(plugin, ()-> startCombat(), (long) new TimeFormatter(Validate.notNull(json.getKey("precombat-start-duration").getAsString(), "Missing precombat-start-duration key", new GameCombatStartException())).convertTo(TimeFormatter.Format.TICKS) + 100L);
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

    public void playerDied(Player player, EntityDamageEvent.DamageCause cause, Entity damager) {
        switch (cause) {
            case SUICIDE -> broadcastDeathMessage("custom.death.cause_suicide",player.getName());
            case PROJECTILE -> broadcastDeathMessage("custom.death.cause_projectile",damager.getCustomName(),player.getName());
            case FIRE, FIRE_TICK, HOT_FLOOR, LAVA -> broadcastDeathMessage("custom.death.cause_fire",player.getName());
            case FALL -> broadcastDeathMessage("custom.death.cause_fall",player.getName());
            case DROWNING -> broadcastDeathMessage("custom.death.cause_drowning",player.getName());
            case POISON, WITHER, MAGIC -> broadcastDeathMessage("custom.death.cause_effect",player.getName());
            case ENTITY_ATTACK, ENTITY_EXPLOSION, ENTITY_SWEEP_ATTACK -> broadcastDeathMessage("custom.death.cause_player",damager.getName(),player.getName());
            default -> broadcastDeathMessage("custom.death.cause_unknown",player.getName());
        }

        player.setGameMode(GameMode.SPECTATOR);
        player.getInventory().clear();
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    private void endGame(GameTeam winner, GameTeam loser) {
        combatTabFooter = Component.text("");

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendPlayerListFooter(
                    combatTabFooter
            );
        });

        Bukkit.broadcast(TranslatableText.basicTranslate("game.combat_winner",winner.getName()));

        winner.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.showTitle(Title.title(TranslatableText.basicTranslate("game.combat.title_win"), Component.text(""))));
        loser.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.showTitle(Title.title(TranslatableText.basicTranslate("game.combat.title_lose"), Component.text(""))));

        BambooFighters.setActualGameCombat(null);

        Bukkit.getScheduler().runTaskLater(plugin, ()->Bukkit.getOnlinePlayers().forEach(player -> {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 10, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 10));
            player.teleport(GameArenaManager.lobby.getCenterLoc());
        }),200L);

        this.currentArena = GameArenaManager.lobby;
        this.configureWorldBorder();
    }

    public void forceEnd() {
        combatTabFooter = Component.text("");

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendPlayerListFooter(
                    combatTabFooter
            );
        });

        Bukkit.broadcast(Component.text("Combat ended by force"));

        BambooFighters.setActualGameCombat(null);
        currentArena.getCenterLoc().getWorld().setPVP(false);

        Bukkit.getScheduler().runTaskLater(plugin, ()->Bukkit.getOnlinePlayers().forEach(player -> {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 10, 10, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 10));
            player.teleport(GameArenaManager.lobby.getCenterLoc());
        }),200L);

        this.currentArena = GameArenaManager.lobby;
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
        var timer = json.getKey("timer-format").getAsString();
        Validate.checkNull(timer, "Missing timer-format key", new GameCombatStartException());

        Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(ChatFormatter.formatMiniMessage(String.format(timer, timerMins, timerSecs))));
    }

    public boolean hasUsedUltimate(Player player) {
        return this.ultimateUsed.contains(player);
    }

    public void playerJustUsedUltimate(Player player) {
        this.ultimateUsed.add(player);
    }

    private void updateTabFooter() {
        String footer = json.getKey("tab-footer").getAsString();
        Validate.checkNull(footer, "Missing tab-footer key", new GameCombatStartException());

        combatTabFooter = ChatFormatter.formatMiniMessage(String.format(footer,
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
        int size = json.getKey("worldborder-initial-size").getAsInt();
        Validate.checkNull(size, "Missing worldborder-initial-size key", new GameCombatStartException());

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

    private void broadcastDeathMessage(String key, String... values) {
        Bukkit.broadcast(TranslatableText.basicTranslate(key, values));
    }
}
