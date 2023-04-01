package io.github.agus5534.bamboofightersv2.game;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.arenas.GameArena;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.utils.text.ComponentManager;
import io.github.agus5534.utils.text.MiniColor;
import io.github.agus5534.utils.text.TranslatableText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("all")
public class GameCombat {

    private final JavaPlugin plugin;
    private final GameArena gameArena;
    private final GameTeam team1;
    private final GameTeam team2;
    private int team1Score, team2Score, mainTask, countdownTask, timerMins, timerSecs;
    private long ticks = 6000;
    private GameArena lobby;
    private List<Player> ultimateUsed;

    private String combatTabFooter;

    public GameCombat(JavaPlugin plugin, GameArena gameArena, GameTeam team1, GameTeam team2) {
        this.plugin = plugin;
        this.gameArena = gameArena;
        this.team1 = team1;
        this.team2 = team2;
        this.ultimateUsed = new ArrayList<>();

        team1Score = 0;
        team2Score = 0;

        Bukkit.getScheduler().runTask(plugin, ()-> preStartCombat());

        Bukkit.getScheduler().runTaskLater(plugin, ()-> startCombat(), 600L);

        combatTabFooter = MiniColor.format(
                "\n%bold%%s %yellow%%d %white%- %yellow%%d %reset% %s%s",
                team1.getTeam().color(),
                team1.getName(),
                team1Score,
                team2Score,
                team2.getTeam().color(),
                team2.getName()
        );

        //TODO set Lobby ARENA
        lobby = GameArena.LOBBY;
    }

    public void incrementTeam1Score() { ++this.team1Score; }
    public void incrementTeam2Score() { ++this.team2Score; }

    private void startCombat() {
        ticks = 6000;
        timerMins = 0;
        timerSecs = 0;
        teleportPlayers();

        team1.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 40, 100));
        });
        team2.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> {
            p.setGameMode(GameMode.SURVIVAL);
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 40, 100));
        });

        team1.giveClasses();
        team2.giveClasses();

        Bukkit.broadcast(TranslatableText.basicTranslate("game.combat_starting",team1.getName(),team2.getName()));
        mainTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->gameTask(),1L,1L);
        gameArena.getTeam1Loc().getWorld().setPVP(true);
    }

    private void preStartCombat() {
        teleportPlayers();
        Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.pre_starting"));
        gameArena.getTeam1Loc().getWorld().setPVP(false);
        gameArena.getTeam1Loc().getWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);
        Bukkit.getOnlinePlayers().forEach(p -> p.setGameMode(GameMode.SPECTATOR));
    }

    private void gameTask() {
        if(ticks > 0) {
            formatTimer();
        }

        if(ticks <= 0) {
            Bukkit.getScheduler().cancelTask(mainTask);

            double t1 = Math.round(teamHealth(team1) * 100.0) / 100.0;
            double t2 = Math.round(teamHealth(team2) * 100.0) / 100.0;

            if(t2 == t1) {
                double toAddOne = Math.round(ThreadLocalRandom.current().nextDouble(1,15) * 100.0) / 100.0;
                double toAddTwo = Math.round(ThreadLocalRandom.current().nextDouble(1,15) * 100.0) / 100.0;

                t1+=toAddOne;
                t2+=toAddTwo;
            }

            if(t1 > t2) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.time_out",team1.getName(),String.valueOf(t1),team2.getName(),String.valueOf(t2)));
                endGame(team1,team2);
                return;
            }

            if(t2 > t1) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.time_out",team2.getName(),String.valueOf(t2),team1.getName(),String.valueOf(t1)));
                endGame(team2,team1);
                return;
            }

            return;
        }

        if(getAlive(team1) == 0) {

            Bukkit.getScheduler().cancelTask(mainTask);

            incrementTeam2Score();

            updateTabFooter();

            if(team2Score < 2) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.round_winner",team2.getName(), String.valueOf(team1Score+team2Score+1)));
                Bukkit.getScheduler().runTaskLater(plugin, ()->startCombat(),200L);
            } else {
                endGame(team2, team1);
            }

        }

        if(getAlive(team2) == 0) {

            Bukkit.getScheduler().cancelTask(mainTask);

            incrementTeam1Score();

            updateTabFooter();

            if(team1Score < 2) {
                Bukkit.broadcast(TranslatableText.basicTranslate("game.combat.round_winner",team1.getName(), String.valueOf(team1Score+team2Score+1)));
                Bukkit.getScheduler().runTaskLater(plugin, ()->startCombat(),200L);
            } else {
                endGame(team1, team2);
            }

        }
    }

    private void teleportPlayers() {
        team1.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.teleport(gameArena.getTeam1Loc()));
        team2.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.teleport(gameArena.getTeam2Loc()));

        Bukkit.getOnlinePlayers().forEach(p -> {
            var t = BambooFighters.playerGameTeamHashMap.get(p);

            if(t.equals(team1) || t.equals(team2)) { return; }

            p.teleport(gameArena.getSpectLoc());
        });
    }

    private int getAlive(GameTeam gameTeam) {
        return gameTeam.getMembers().stream()
                .filter(p -> p.isOnline())
                .filter(p -> p.getGameMode() == GameMode.SURVIVAL)
                .toList().size();
    }

    private void endGame(GameTeam winner, GameTeam loser) {
        combatTabFooter = "";
        updateTabFooter();
        Bukkit.broadcast(TranslatableText.basicTranslate("game.combat_winner",winner.getName()));

        winner.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.showTitle(Title.title(TranslatableText.basicTranslate("game.combat.title_win"), Component.text(""))));
        loser.getMembers().stream().filter(p -> p.isOnline()).forEach(p -> p.showTitle(Title.title(TranslatableText.basicTranslate("game.combat.title_lose"), Component.text(""))));

        BambooFighters.setActualGameCombat(null);

        Bukkit.getScheduler().runTaskLater(plugin, ()->Bukkit.getOnlinePlayers().forEach(player -> {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 200, 10));
            player.teleport(lobby.getSpectLoc());
        }),200L);
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

    private double teamHealth(GameTeam gameTeam) {
        AtomicReference<Double> d = new AtomicReference<>(0.0);

        gameTeam.getMembers().stream()
                .filter(p -> p.isOnline())
                .filter(p -> p.getGameMode() == GameMode.SURVIVAL)
                .forEach(p -> d.updateAndGet(v -> new Double((double) (v + p.getHealth()))));

        return d.get();
    }

    public boolean hasUsedUltimate(Player player) {
        return this.ultimateUsed.contains(player);
    }

    public void playerJustUsedUltimate(Player player) {
        this.ultimateUsed.add(player);
    }

    private void updateTabFooter() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendPlayerListFooter(
                    ComponentManager.formatMiniMessage(combatTabFooter)
            );
        });
    }
}
