package io.github.agus5534.bamboofightersv2.menus;

import io.github.agus5534.bamboofightersv2.BambooFighters;
import io.github.agus5534.bamboofightersv2.arenas.GameArena;
import io.github.agus5534.bamboofightersv2.arenas.GameArenaManager;
import io.github.agus5534.bamboofightersv2.game.GameCombat;
import io.github.agus5534.bamboofightersv2.team.GameTeam;
import io.github.agus5534.utils.items.ItemCreator;
import io.github.agus5534.utils.text.ComponentManager;
import io.github.agus5534.utils.text.TranslatableText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.item.action.ItemClickableAction;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.validate.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombatSelectionMenu {
    private final BambooFighters plugin;
    private ItemStack borderItem;
    private ItemStack noEntity;


    public CombatSelectionMenu(BambooFighters plugin) {
        this.plugin = plugin;
        borderItem = new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name(" ");
        noEntity = new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ");
    }

    public Inventory getArenaSelector() {
        List<GameArena> gameArenas = new ArrayList<>();
        return MenuInventory.newPaginatedBuilder(GameArena.class, "Selector de Arena")
                .itemIfNoEntities(ItemClickable.onlyItem(noEntity))
                .entityParser(gameArena -> ItemClickable.builder().item(new ItemCreator(gameArena.getArenaIcon()).name(gameArena.getArenaName())).action(event -> {
                    if(!(event.getWhoClicked() instanceof Player)) { return true; }

                    var p = (Player)event.getWhoClicked();

                    if(!gameArenas.contains(gameArena)) {
                        gameArenas.add(gameArena);

                        var ic = new ItemCreator(event.getCurrentItem());
                        ic.enchants(Enchantment.ARROW_FIRE, 1);
                        ic.addItemFlag(ItemFlag.HIDE_ENCHANTS);

                        event.getCurrentItem().setItemMeta(ic.getItemMeta());
                    } else {
                        gameArenas.remove(gameArena);

                        var ic = new ItemCreator(event.getCurrentItem());
                        ic.removeEnchantments();

                        event.getCurrentItem().setItemMeta(ic.getItemMeta());
                    }

                    return true;
                }).build())
                .nextPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.DIAMOND).name(ChatColor.translateAlternateColorCodes('&',"Siguiente Página"))))
                .previousPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.GOLD_INGOT).name(ChatColor.translateAlternateColorCodes('&',"Anterior Página"))))
                .itemIfNoNextPage(ItemClickable.onlyItem(borderItem))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(borderItem))
                .entities(GameArenaManager.arenas)
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
                        "xpxxsxxnx"
                )
                .layoutItem('x', ItemClickable.onlyItem(borderItem))
                .layoutItem('s', ItemClickable.builder().item(new ItemCreator(Material.DIAMOND_SWORD).name(ComponentManager.formatMiniMessage("<blue>Siguiente Paso"))).action(event -> {
                    var p = (Player)event.getWhoClicked();

                    if(gameArenas.size() < 3) {
                        return true;
                    }

                    p.closeInventory();

                    p.openInventory(getTeamOneSelector(gameArenas));

                    return true;
                }).build())
                .build();

    }


    public Inventory getTeamOneSelector(List<GameArena> gameArenas) {
        return MenuInventory.newPaginatedBuilder(GameTeam.class, "Selector de Team 1")
                .itemIfNoEntities(ItemClickable.onlyItem(noEntity))
                .entityParser(gameTeam -> ItemClickable.builder().item(new ItemCreator(Material.PLAYER_HEAD).setSkullSkin(gameTeam.getOwner()).name(gameTeam.getName())).action(event -> {
                    if(!(event.getWhoClicked() instanceof Player)) { return true; }

                    var p = (Player)event.getWhoClicked();
                    p.closeInventory();

                    p.sendMessage(TranslatableText.basicTranslate("command.startcombat.select_teamone",gameTeam.getName()));

                    p.openInventory(getTeamTwoSelector(gameArenas, gameTeam));

                    return true;
                }).build())
                .nextPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.DIAMOND).name(ChatColor.translateAlternateColorCodes('&',"Siguiente Página"))))
                .previousPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.GOLD_INGOT).name(ChatColor.translateAlternateColorCodes('&',"Anterior Página"))))
                .itemIfNoNextPage(ItemClickable.onlyItem(borderItem))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(borderItem))
                .entities(BambooFighters.getGameTeams())
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

    public Inventory getTeamTwoSelector(List<GameArena> gameArenas, GameTeam team1) {
        return MenuInventory.newPaginatedBuilder(GameTeam.class, "Selector de Team 2")
                .itemIfNoEntities(ItemClickable.onlyItem(noEntity))
                .entityParser(gameTeam -> ItemClickable.builder().item(new ItemCreator(Material.PLAYER_HEAD).setSkullSkin(gameTeam.getOwner()).name(gameTeam.getName())).action(event -> {
                    if(!(event.getWhoClicked() instanceof Player)) { return true; }

                    var p = (Player)event.getWhoClicked();
                    p.closeInventory();

                    p.sendMessage(TranslatableText.basicTranslate("command.startcombat.select_teamtwo",gameTeam.getName()));

                    Bukkit.broadcast(TranslatableText.basicTranslate("game.combat_next",team1.getName(), gameTeam.getName()));

                    Bukkit.getScheduler().runTaskLater(plugin, ()-> BambooFighters.setActualGameCombat(new GameCombat(plugin,gameArenas,team1,gameTeam)),300L);

                    return true;
                }).build())
                .nextPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.DIAMOND).name(ChatColor.translateAlternateColorCodes('&',"Siguiente Página"))))
                .previousPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.GOLD_INGOT).name(ChatColor.translateAlternateColorCodes('&',"Anterior Página"))))
                .itemIfNoNextPage(ItemClickable.onlyItem(borderItem))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(borderItem))
                .entities(BambooFighters.getGameTeams().stream().filter(gameTeam -> gameTeam != team1).toList())
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
}
