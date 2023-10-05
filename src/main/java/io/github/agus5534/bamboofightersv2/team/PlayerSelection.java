package io.github.agus5534.bamboofightersv2.team;

import io.github.agus5534.agusutils.utils.scoreboard.MainScoreboard;
import io.github.agus5534.agusutils.utils.text.TranslatableText;
import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.menus.TeamSelectionMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PlayerSelection {

    private final SelectionTier selectionTier;
    private final Iterator<GameTeam> gameTeams;
    private Player toSelect;
    private final BambooFighters plugin;
    private boolean hasSelected;
    public boolean a;
    private int tskID;

    public PlayerSelection(SelectionTier selectionTier, List<GameTeam> gameTeams, BambooFighters plugin) {
        this.selectionTier = selectionTier;

        this.gameTeams = this.shuffleList(gameTeams, 8).iterator();

        this.plugin = plugin;

        hasSelected = true;
        a = false;
    }

    public enum SelectionTier {
        TIER_1,
        TIER_2,
        TIER_3,
        TIER_4;
    }

    public List<GameTeam> shuffleList(List<GameTeam> gameTeams, int times) {
        int i = 0;

        while (i < times) {
            i++;
            Collections.shuffle(gameTeams, new Random());
        }

        return gameTeams;
    }

    public void startSelection() {
        tskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            if(!hasSelected) {
                if(this.getToSelect() == null) { return; }
                if(!this.getToSelect().getOpenInventory().getTitle().equalsIgnoreCase("Selección de jugadores") && !a) {
                    this.getToSelect().openInventory(new TeamSelectionMenu().getInventory(this));
                    return;
                }

                return;
            }

            if(!gameTeams.hasNext()) {
                cancelTask();
                Bukkit.broadcast(TranslatableText.basicTranslate("team.selection.round_ended",selectionTier.toString().replaceAll("_", "")));
                return;
            }

            var team = gameTeams.next();
            toSelect = team.getOwner();

            Bukkit.broadcast(TranslatableText.basicTranslate("team.selection.new_turn", toSelect.getName()));

            toSelect.openInventory(new TeamSelectionMenu().getInventory(this));
            hasSelected = false;
            a = false;
        },1L,7L);
    }

    public List<Player> getPlayers() {
        String tier = selectionTier.name().replace("_","");

        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> !BambooFighters.playerGameTeamHashMap.containsKey(p))
                .filter(p -> MainScoreboard.getScore(tier,p.getName()) != null)
                .filter(p -> MainScoreboard.getScore(tier,p.getName()).getScore() != 0)
                .collect(Collectors.toList());
    }

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }

    public Player getToSelect() {
        return toSelect;
    }

    public void cancelTask() {
        if(Bukkit.getScheduler().isCurrentlyRunning(tskID)) {
            Bukkit.getScheduler().cancelTask(tskID);
        }
    }

    public SelectionTier getSelectionTier() {
        return selectionTier;
    }
}
