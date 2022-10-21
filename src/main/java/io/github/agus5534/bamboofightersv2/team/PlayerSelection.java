package io.github.agus5534.bamboofightersv2.team;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.menus.TeamSelectionMenu;
import io.github.agus5534.utils.scoreboard.MainScoreboard;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PlayerSelection {

    private final SelectionTier selectionTier;
    private final List<GameTeam> gameTeams;
    private Player toSelect;
    private final BambooFighters plugin;
    private boolean hasSelected;
    private int i = 0, tskID;

    public PlayerSelection(SelectionTier selectionTier, List<GameTeam> gameTeams, BambooFighters plugin) {
        this.selectionTier = selectionTier;

        this.gameTeams = gameTeams;

        this.plugin = plugin;

        this.shuffleList(8);

        hasSelected = true;
    }

    public enum SelectionTier {
        TIER_1,
        TIER_2,
        TIER_3,
        TIER_4;
    }

    public void shuffleList(int times) {
        int i = 0;

        while (i < times) {
            i++;
            Collections.shuffle(gameTeams, new Random());
        }
    }

    public void startSelection() {
        tskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            if(!hasSelected) { return; }

            if(i >= getPlayers().size()) {
                cancelTask();
                Bukkit.broadcast(TranslatableText.basicTranslate("team.selection.round_ended",selectionTier.toString()));
                return;
            }

            toSelect = gameTeams.get(i).getOwner();

            toSelect.openInventory(new TeamSelectionMenu().getInventory(this));
            hasSelected = false;
            i++;
        },1L,20L);
    }

    public List<Player> getPlayers() {
        String tier = selectionTier.toString().replace("_","");

        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> !BambooFighters.playerGameTeamHashMap.containsKey(p))
                .filter(p -> MainScoreboard.getScore(tier,p.getName()) != null)
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
}
