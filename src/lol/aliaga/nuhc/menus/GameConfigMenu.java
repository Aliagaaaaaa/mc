package lol.aliaga.nuhc.menus;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameConfigMenu {

    private final Inventory menu;

    public GameConfigMenu() {
        menu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Game Configuration");
        loadMenuItems();
    }

    // Método para abrir el menú
    public void openMenu(Player player) {
        player.openInventory(menu);
    }

    // Cargar ítems del menú según la configuración actual
    private void loadMenuItems() {
        GameConfig config = NUHC.getInstance().getGameConfig();
        ItemStack[] menuItems = {
                createMenuItem(Material.BOOK, "Teams", ChatColor.GRAY + "Current: " + config.getTeams()),
                createMenuItem(Material.BEDROCK, "Border", ChatColor.GRAY + "Current: " + config.getBorder() + " blocks"),
                createMenuItem(Material.WATCH, "Border Shrinking", ChatColor.GRAY + "Starts shrinking at: " + config.getBorderShrinking() + " minutes"),
                createMenuItem(config.isNether() ? Material.NETHER_STAR : Material.NETHERRACK, "Nether", ChatColor.GRAY + "Enabled: " + config.isNether()),
                createMenuItem(Material.POTION, "Speed", ChatColor.GRAY + "Current: Speed " + config.getSpeed()),
                createMenuItem(Material.BLAZE_POWDER, "Strength", ChatColor.GRAY + "Current: Strength " + config.getStrength()),
                createMenuItem(Material.SPIDER_EYE, "Poison", ChatColor.GRAY + "Current: Poison " + config.getPoison()),
                createMenuItem(Material.APPLE, "Apple Rate", ChatColor.GRAY + "Rate: " + config.getAppleRate()),
                createMenuItem(Material.FLINT, "Flint Rate", ChatColor.GRAY + "Rate: " + config.getFlintRate()),
                createMenuItem(Material.IRON_SWORD, "PvP Time", ChatColor.GRAY + "Current: " + config.getPvpTime() + " minutes"),
                createMenuItem(Material.GOLDEN_APPLE, "Final Heal Time", ChatColor.GRAY + "Current: " + config.getFinalHealTime() + " minutes"),
                createMenuItem(Material.ANVIL, "Practice Mode", ChatColor.GRAY + "Enabled: " + config.isPractice()),
                createMenuItem(Material.ENDER_PEARL, "Enderpearl", ChatColor.GRAY + "Enabled: " + config.isEnderpearl())
        };

        for (int i = 0; i < menuItems.length; i++) {
            menu.setItem(i, menuItems[i]);
        }
    }

    // Crear ítem para el menú con nombre y descripción
    private ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + name);
            List<String> loreList = new ArrayList<>();
            Collections.addAll(loreList, lore);
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        return item;
    }
}
