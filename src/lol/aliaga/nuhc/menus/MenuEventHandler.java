package lol.aliaga.nuhc.menus;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.InventoryView;

public class MenuEventHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        String inventoryTitle = ChatColor.stripColor(inventory.getTitle());
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        if (inventoryTitle.equals("Game Configuration") || inventoryTitle.startsWith("Edit ")) {
            event.setCancelled(true);

            if (inventoryTitle.equals("Game Configuration")) {
                handleConfigMenuClick(player, clickedItem);
            } else if (inventoryTitle.startsWith("Edit ")) {
                handleEditMenuClick(player, inventoryTitle, clickedItem);
            }
        }
    }

    // Manejar clic en el menú de configuración principal
    private void handleConfigMenuClick(Player player, ItemStack clickedItem) {
        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        if (player.hasPermission("uhc.edit")) {
            switch (itemName) {
                case "Final Heal Time":
                    openEditMenu(player, "finalheal");
                    break;
                case "Border":
                    openEditMenu(player, "border");
                    break;
                case "Border Shrinking":
                    openEditMenu(player, "bordershrinking");
                    break;
                case "Teams":
                    openEditMenu(player, "teams");
                    break;
                case "Speed":
                    openEditMenu(player, "speed");
                    break;
                case "Strength":
                    openEditMenu(player, "strength");
                    break;
                case "Poison":
                    openEditMenu(player, "poison");
                    break;
                case "Apple Rate":
                    openEditMenu(player, "applerate");
                    break;
                case "Flint Rate":
                    openEditMenu(player, "flintrate");
                    break;
                case "PvP Time":
                    openEditMenu(player, "pvptime");
                    break;
                case "Nether":
                    openEditMenu(player, "nether");
                    break;
                case "Enderpearl":
                    openEditMenu(player, "enderpearl");
                    break;
                case "Practice Mode":
                    openEditMenu(player, "practice");
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Unknown option: " + itemName);
            }
        }
    }

    // Manejar clic en el menú de edición
    private void handleEditMenuClick(Player player, String menuTitle, ItemStack clickedItem) {
        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        GameConfig config = NUHC.getInstance().getGameConfig();
        String configName = menuTitle.replace("Edit ", "").toLowerCase();

        if ("Go Back".equals(itemName)) {
            new GameConfigMenu().openMenu(player);
            return;
        }

        switch (itemName) {
            case "Decrease":
                handleDecrease(configName, config);
                break;
            case "Increase":
                handleIncrease(configName, config);
                break;
            case "Toggle":
                toggleBoolean(configName, config);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown action: " + itemName);
        }
        new EditConfigMenu(configName).openMenu(player);
    }

    // Lógica para disminuir valores en el menú de edición
    private void handleDecrease(String configName, GameConfig config) {
        switch (configName) {
            case "finalheal":
                config.setFinalHealTime(Math.max(0, config.getFinalHealTime() - 1));
                break;
            case "border":
                config.setBorder(getBorderValue(getCurrentBorderIndex(config.getBorder()) + 1));
                break;
            case "bordershrinking":
                config.setBorderShrinking(Math.max(5, config.getBorderShrinking() - 5));
                break;
            case "teams":
                config.setTeams(Math.max(1, config.getTeams() - 1));
                break;
            case "speed":
            case "strength":
            case "poison":
                decreaseEffect(configName, config);
                break;
            case "applerate":
                config.setAppleRate(Math.max(0, config.getAppleRate() - 0.1f));
                break;
            case "flintrate":
                config.setFlintRate(Math.max(0, config.getFlintRate() - 0.1f));
                break;
            case "pvptime":
                config.setPvpTime(Math.max(0, config.getPvpTime() - 1));
                break;
            default:
                break;
        }
    }

    // Lógica para aumentar valores en el menú de edición
    private void handleIncrease(String configName, GameConfig config) {
        switch (configName) {
            case "finalheal":
                config.setFinalHealTime(config.getFinalHealTime() + 1);
                break;
            case "border":
                config.setBorder(getBorderValue(getCurrentBorderIndex(config.getBorder()) - 1));
                break;
            case "bordershrinking":
                config.setBorderShrinking(config.getBorderShrinking() + 5);
                break;
            case "teams":
                config.setTeams(config.getTeams() + 1);
                break;
            case "speed":
            case "strength":
            case "poison":
                increaseEffect(configName, config);
                break;
            case "applerate":
                config.setAppleRate(config.getAppleRate() + 0.1f);
                break;
            case "flintrate":
                config.setFlintRate(config.getFlintRate() + 0.1f);
                break;
            case "pvptime":
                config.setPvpTime(config.getPvpTime() + 1);
                break;
            default:
                break;
        }
    }

    // Alternar valores booleanos en el menú de edición
    private void toggleBoolean(String configName, GameConfig config) {
        switch (configName) {
            case "nether":
                config.setNether(!config.isNether());
                break;
            case "enderpearl":
                config.setEnderpearl(!config.isEnderpearl());
                break;
            case "practice":
                config.setPractice(!config.isPractice());
                break;
            default:
                break;
        }
    }

    // Disminuir valores de efectos específicos
    private void decreaseEffect(String effectType, GameConfig config) {
        switch (effectType) {
            case "speed":
                config.setSpeed(Math.max(0, config.getSpeed() - 1));
                break;
            case "strength":
                config.setStrength(Math.max(0, config.getStrength() - 1));
                break;
            case "poison":
                config.setPoison(Math.max(0, config.getPoison() - 1));
                break;
            default:
                break;
        }
    }

    // Aumentar valores de efectos específicos
    private void increaseEffect(String effectType, GameConfig config) {
        switch (effectType) {
            case "speed":
                config.setSpeed(Math.min(2, config.getSpeed() + 1));
                break;
            case "strength":
                config.setStrength(Math.min(2, config.getStrength() + 1));
                break;
            case "poison":
                config.setPoison(Math.min(2, config.getPoison() + 1));
                break;
            default:
                break;
        }
    }

    // Abrir menú de edición específico
    private void openEditMenu(Player player, String configName) {
        new EditConfigMenu(configName).openMenu(player);
    }

    // Valores de bordes predeterminados y sus índices
    private final int[] borderValues = {3000, 2000, 1500, 1000, 500, 250, 100, 50};

    private int getCurrentBorderIndex(int currentBorder) {
        for (int i = 0; i < borderValues.length; i++) {
            if (borderValues[i] == currentBorder) return i;
        }
        return 0;  // Predeterminado al índice 0
    }

    private int getBorderValue(int index) {
        if (index < 0) return borderValues[borderValues.length - 1];
        if (index >= borderValues.length) return borderValues[0];
        return borderValues[index];
    }
}
