package io.github.agus5534.bamboofightersv2.menus;

import io.github.agus5534.agusutils.utils.text.TranslatableText;
import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.classes.GameClass;
import io.github.agus5534.bamboofightersv2.utils.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

public class ClassSelectionMenu {

    BambooFighters plugin;

    private ItemStack borderItem;
    private ItemStack noEntity;

    public ClassSelectionMenu(BambooFighters plugin) {
        this.plugin = plugin;
        borderItem = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build();
        noEntity = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
    }

    public Inventory getInventory() {
        return MenuInventory.newPaginatedBuilder(GameClass.class, "Selector de Clases")
                .entities(plugin.getGameClasses())
                .itemsPerRow(7)
                .entityParser(gameClass -> ItemClickable.builder().item(new ItemBuilder(gameClass.getClassMaterial()).setDisplayName(gameClass.getClassName()).build()).action(event -> {
                    if(!(event.getWhoClicked() instanceof Player)) { return true; }

                    var p = (Player)event.getWhoClicked();
                    var pTeam = BambooFighters.playerGameTeamHashMap.get(p);

                    if(pTeam == null) {
                        p.sendMessage(TranslatableText.basicTranslate("command.selectclass.missing_team"));
                        p.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
                        return true;
                    }

                    pTeam.setPlayerClass(p,gameClass);

                    p.sendMessage(TranslatableText.basicTranslate("command.selectclass.class_selected",gameClass.getClassName()));

                    return true;

                }).build())
                .bounds(10,44)
                .itemIfNoEntities(ItemClickable.onlyItem(noEntity))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(borderItem))
                .itemIfNoNextPage(ItemClickable.onlyItem(borderItem))
                .nextPageItem(p -> ItemClickable.onlyItem(new ItemBuilder(Material.DIAMOND).setDisplayName(ChatColor.translateAlternateColorCodes('&',"Siguiente Página")).build()))
                .previousPageItem(p -> ItemClickable.onlyItem(new ItemBuilder(Material.GOLD_INGOT).setDisplayName(ChatColor.translateAlternateColorCodes('&',"Anterior Página")).build()))
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
}
