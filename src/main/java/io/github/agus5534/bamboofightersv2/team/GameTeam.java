package io.github.agus5534.bamboofightersv2.team;

import io.github.agus5534.agusutils.utils.text.TranslatableText;
import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class GameTeam {
    private final Player owner;
    private List<Player> members;
    private final String name;
    private Team team;
    private NamedTextColor color;

    private HashMap<Player, GameClass> playerGameClassHashMap;

    public GameTeam(Player owner) {
        this.owner = owner;
        this.members = new ArrayList<>();
        this.name = owner.getName() + " Team";

        createTeam();
        playerGameClassHashMap = new HashMap<>();
        Bukkit.broadcast(TranslatableText.basicTranslate("team.created",team.getName()));
        addMember(owner);
    }

    public String getName() {
        return name;
    }

    public List<Player> getMembers() {
        return members;
    }

    public Player getOwner() {
        return owner;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    private void createTeam() {
        var tm = Optional.ofNullable(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(owner.getName()));

        if(tm.isPresent()) {
            setTeam(tm.get());
        } else {
            setTeam(Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(owner.getName()));
        }

        BambooFighters.addGameTeam(this);

        getTeam().prefix(Component.text("[" + this.name + "] "));
    }

    public void addMember(Player player) {
        getMembers().add(player);

        getTeam().addEntry(player.getName());

        player.sendMessage(TranslatableText.basicTranslate("team.player_join",owner.getName()));

        owner.sendMessage(TranslatableText.basicTranslate("team.owner.player_join",player.getName()));

        player.teleport(owner.getLocation());

        BambooFighters.playerGameTeamHashMap.put(player, this);
    }

    public void setPlayerClass(Player player, GameClass gameClass) {
        getPlayerGameClassHashMap().put(player,gameClass);
    }

    public HashMap<Player, GameClass> getPlayerGameClassHashMap() {
        return playerGameClassHashMap;
    }

    public void giveClasses() {
        getMembers().stream().filter(OfflinePlayer::isOnline).forEach(p -> {
            p.getInventory().clear();
            var c = playerGameClassHashMap.get(p);

            if(!playerGameClassHashMap.containsKey(p)) {
                c = BambooFighters.staticGameClass.get(new Random().nextInt(BambooFighters.staticGameClass.size()));
            }

            for(int i : c.getClassItems().keySet()) {
                p.getInventory().setItem(i, c.getClassItems().get(i));
            }
        });
    }

    public NamedTextColor getColor() {
        return color;
    }

    public void setColor(NamedTextColor color) {
        this.color = color;

        getTeam().color(this.color);
    }
}
