package io.github.agus5534.bamboofightersv2.menus;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.team.PlayerSelection;
import io.github.agus5534.bamboofightersv2.utils.location.OvniAnimation;
import io.github.agus5534.utils.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

public class TeamSelectionMenu {

    private ItemStack borderItem;
    private ItemStack noEntity;
    public TeamSelectionMenu() {
        borderItem = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ");
        noEntity = new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ");
    }
    public Inventory getInventory(PlayerSelection playerSelection) {
        return MenuInventory.newPaginatedBuilder(Player.class, "Selección de jugadores")
                .itemIfNoEntities(ItemClickable.onlyItem(noEntity))
                .entityParser(player -> ItemClickable.builder().item(new ItemCreator(Material.PLAYER_HEAD).setSkullSkin(player).name(player.getName())).action(event -> {
                    if(!(event.getWhoClicked() instanceof Player)) { return true; }

                    var p = (Player)event.getWhoClicked();

                    playerSelection.a = true;
                    p.closeInventory();

                    var team = BambooFighters.playerGameTeamHashMap.get(playerSelection.getToSelect());

                    new OvniAnimation(player, team, playerSelection);

                    return true;
                }).build())
                .nextPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.DIAMOND).name(ChatColor.translateAlternateColorCodes('&',"Siguiente Página"))))
                .previousPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.GOLD_INGOT).name(ChatColor.translateAlternateColorCodes('&',"Anterior Página"))))
                .itemIfNoNextPage(ItemClickable.onlyItem(borderItem))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(borderItem))
                .entities(playerSelection.getPlayers())
                .bounds(10,44)
                .itemsPerRow(7)
                .introduceItems(false)
                .fillBorders(ItemClickable.onlyItem(borderItem))
                .closeAction(inventory -> {
                    if(!playerSelection.isHasSelected()) {
                        playerSelection.getToSelect().openInventory(getInventory(playerSelection));
                    }

                    return true;
                })
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
}
