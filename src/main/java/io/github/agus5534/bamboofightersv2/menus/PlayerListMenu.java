package io.github.agus5534.bamboofightersv2.menus;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.utils.items.ItemCreator;
import io.github.agus5534.utils.scoreboard.MainScoreboard;
import io.github.agus5534.utils.text.ComponentManager;
import io.github.agus5534.utils.text.MiniColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import java.util.Arrays;

public class PlayerListMenu {
    private ItemStack borderItem;
    private ItemStack noEntity;

    public PlayerListMenu() {
        borderItem = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ");
        noEntity = new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ");
    }

    public Inventory getInventory() {
        return MenuInventory.newPaginatedBuilder(OfflinePlayer.class, "Jugadores")
                .itemIfNoEntities(ItemClickable.onlyItem(noEntity))
                .entityParser(offlinePlayer -> ItemClickable.onlyItem(
                        new ItemCreator(Material.PLAYER_HEAD).setSkullSkin(offlinePlayer)
                                .name(offlinePlayer.getName())
                                .lore(ComponentManager.formatMiniMessage(MiniColor.GRAY + getTier(offlinePlayer)),
                                        ComponentManager.formatMiniMessage(MiniColor.GRAY + "Team: " + getTeam(offlinePlayer)))
                ))
                .nextPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.DIAMOND).name(ChatColor.translateAlternateColorCodes('&',"Siguiente Página"))))
                .previousPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.GOLD_INGOT).name(ChatColor.translateAlternateColorCodes('&',"Anterior Página"))))
                .itemIfNoNextPage(ItemClickable.onlyItem(borderItem))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(borderItem))
                .entities(Arrays.stream(Bukkit.getOfflinePlayers()).toList())
                .bounds(10, 44)
                .itemsPerRow(7)
                .introduceItems(false)
                .fillBorders(ItemClickable.onlyItem(borderItem))
                .layoutLines(
                        "xxxxxxxxx",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "xeeeeeeex",
                        "xpxxxxxnx"
                )
                .layoutItem('x', ItemClickable.onlyItem(borderItem))
                .build();
    }


    public String getTier(OfflinePlayer offlinePlayer) {
        String s = "Ninguno";


        var tier1 = MainScoreboard.getScore("TIER1", offlinePlayer.getName()).getScore();
        var tier2 = MainScoreboard.getScore("TIER2", offlinePlayer.getName()).getScore();
        var tier3 = MainScoreboard.getScore("TIER3", offlinePlayer.getName()).getScore();
        var tier4 = MainScoreboard.getScore("TIER4", offlinePlayer.getName()).getScore();

        if(tier1 > 0) { s = "Tier 1"; }
        if(tier2 > 0) { s = "Tier 2"; }
        if(tier3 > 0) { s = "Tier 3"; }
        if(tier4 > 0) { s = "Tier 4"; }

        return s;
    }

    public String getTeam(OfflinePlayer offlinePlayer) {
        var plTeam = BambooFighters.playerGameTeamHashMap.get(offlinePlayer);

        return (plTeam == null ? "Ninguno" : plTeam.getName());
    }





}
