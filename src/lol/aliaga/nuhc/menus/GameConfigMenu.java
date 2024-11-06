package lol.aliaga.nuhc.menus;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GameConfigMenu implements Listener {

    private final Inventory menu;

    public GameConfigMenu() {
        menu = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Game Configuration");
        loadMenuItems();
    }

    public void openMenu(Player player) {
        player.openInventory(menu);
    }

    private void loadMenuItems() {
        GameConfig config = NUHC.getInstance().getGameConfig();

        // Crear ítems para cada configuración
        ItemStack[] menuItems = {
                createMenuItem(Material.BOOK, "Teams", "Current: " + config.getTeams()),
                createMenuItem(Material.BEDROCK, "Border", "Current: " + config.getBorder() + " blocks"),
                createMenuItem(Material.WATCH, "Border Shrinking", "Starts shrinking at: " + config.getBorderShrinking() + " minutes"),
                createMenuItem(config.isNether() ? Material.NETHER_STAR : Material.NETHERRACK, "Nether", "Enabled: " + config.isNether()),
                createMenuItem(Material.POTION, "Speed", "Current: Speed " + config.getSpeed()),
                createMenuItem(Material.BLAZE_POWDER, "Strength", "Current: Strength " + config.getStrength()),
                createMenuItem(Material.SPIDER_EYE, "Poison", "Current: Poison " + config.getPoison()),
                createMenuItem(Material.APPLE, "Apple Rate", "Rate: " + config.getAppleRate()),
                createMenuItem(Material.FLINT, "Flint Rate", "Rate: " + config.getFlintRate()),
                createMenuItem(Material.IRON_SWORD, "PvP Time", "Current: " + config.getPvpTime() + " minutes"),
                createMenuItem(Material.GOLDEN_APPLE, "Final Heal Time", "Current: " + config.getFinalHealTime() + " minutes"),
                createMenuItem(Material.ANVIL, "Practice Mode", "Enabled: " + config.isPractice()),
                createMenuItem(Material.ENDER_PEARL, "Enderpearl", "Enabled: " + config.isEnderpearl())
        };

        // Colocar los ítems en el menú
        for (int i = 0; i < menuItems.length; i++) {
            menu.setItem(i, menuItems[i]);
        }
    }

    private ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + name);
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(ChatColor.GRAY + line);
            }
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        return item;
    }
}
