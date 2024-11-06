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

import java.util.Arrays;

public class EditConfigMenu {

    private final Inventory menu;
    private final String configName;

    public EditConfigMenu(String configName) {
        this.configName = configName;
        menu = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Edit " + configName);
        loadMenuItems();
    }

    public void openMenu(Player player) {
        player.openInventory(menu);
    }

    private void loadMenuItems() {
        GameConfig config = NUHC.getInstance().getGameConfig();
        ItemStack decreaseItem;
        ItemStack valueItem;
        ItemStack increaseItem;

        // Flecha para regresar al menú principal
        ItemStack backArrow = createMenuItem(Material.ARROW, "Go Back", "Return to main menu");

        switch (configName.toLowerCase()) {
            case "finalheal":
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Decrease", "-1");
                valueItem = createMenuItem(Material.PAPER, "Current Value", config.getFinalHealTime() + " minutes");
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Increase", "+1");
                break;
            case "border":
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Decrease", "Lower Border");
                valueItem = createMenuItem(Material.PAPER, "Current Border", config.getBorder() + " blocks");
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Increase", "Increase Border");
                break;
            case "teams":
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Decrease", "-1");
                valueItem = createMenuItem(Material.PAPER, "Current Teams", String.valueOf(config.getTeams()));
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Increase", "+1");
                break;
            case "bordershrinking":
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Decrease", "-5 minutes");
                valueItem = createMenuItem(Material.PAPER, "Current Shrinking Time", config.getBorderShrinking() + " minutes");
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Increase", "+5 minutes");
                break;
            case "speed":
            case "strength":
            case "poison":
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Decrease", "-1");
                valueItem = createMenuItem(Material.PAPER, "Current Value", "Level " + getCurrentEffectValue(configName, config));
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Increase", "+1");
                break;
            case "applerate":
            case "flintrate":
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Decrease", "-0.1");
                valueItem = createMenuItem(Material.PAPER, "Current Rate", String.valueOf(getCurrentFloatValue(configName, config)));
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Increase", "+0.1");
                break;
            case "nether":
            case "enderpearl":
            case "practice":
                boolean currentBool = getCurrentBooleanValue(configName, config);
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Toggle", "Switch to " + !currentBool);
                valueItem = createMenuItem(Material.PAPER, "Current State", String.valueOf(currentBool));
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Toggle", "Switch to " + !currentBool);
                break;
            case "pvptime":
                decreaseItem = createMenuItem(Material.REDSTONE_BLOCK, "Decrease", "-1 minute");
                valueItem = createMenuItem(Material.PAPER, "Current PvP Time", config.getPvpTime() + " minutes");
                increaseItem = createMenuItem(Material.EMERALD_BLOCK, "Increase", "+1 minute");
                break;
            default:
                decreaseItem = createMenuItem(Material.BARRIER, "Unknown", "N/A");
                valueItem = createMenuItem(Material.BARRIER, "Unknown", "N/A");
                increaseItem = createMenuItem(Material.BARRIER, "Unknown", "N/A");
        }

        // Colocar los ítems en el menú
        menu.setItem(3, decreaseItem);
        menu.setItem(4, valueItem);
        menu.setItem(5, increaseItem);

        // Flecha para regresar en el slot 8
        menu.setItem(8, backArrow);
    }

    private ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    private int getCurrentEffectValue(String effectType, GameConfig config) {
        switch (effectType.toLowerCase()) {
            case "speed":
                return config.getSpeed();
            case "strength":
                return config.getStrength();
            case "poison":
                return config.getPoison();
            default:
                return 0;
        }
    }

    private float getCurrentFloatValue(String floatType, GameConfig config) {
        switch (floatType.toLowerCase()) {
            case "applerate":
                return config.getAppleRate();
            case "flintrate":
                return config.getFlintRate();
            default:
                return 0;
        }
    }

    private boolean getCurrentBooleanValue(String booleanType, GameConfig config) {
        switch (booleanType.toLowerCase()) {
            case "nether":
                return config.isNether();
            case "enderpearl":
                return config.isEnderpearl();
            case "practice":
                return config.isPractice();
            default:
                return false;
        }
    }
}
