package lol.aliaga.nuhc.menus;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class ScenariosMenu implements Listener {

    public void openMenu(Player player) {
        List<Scenario> activeScenarios = NUHC.getInstance().getGameConfig().getScenarios();
        int size = Math.max((int) Math.ceil(activeScenarios.size() / 9.0) * 9, 9); // Ensure size is at least 9
        Inventory menu = Bukkit.createInventory(null, size, ChatColor.GREEN + "Active Scenarios");

        for (Scenario scenario : activeScenarios) {
            ItemStack item = createScenarioItem(scenario);
            menu.addItem(item);
        }

        if (player.hasPermission("nuhc.admin.editscenarios")) {
            ItemStack editItem = createEditItem();
            menu.setItem(menu.getSize() - 1, editItem);  // Place at the last slot
        }

        player.openInventory(menu);
    }

    private ItemStack createScenarioItem(Scenario scenario) {
        ItemStack item = new ItemStack(scenario.getIcon());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + scenario.getName());
            meta.setLore(Collections.singletonList(ChatColor.GRAY + scenario.information()));
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createEditItem() {
        ItemStack editItem = new ItemStack(Material.ANVIL);
        ItemMeta editMeta = editItem.getItemMeta();
        if (editMeta != null) {
            editMeta.setDisplayName(ChatColor.RED + "Edit Scenarios");
            editMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to edit scenarios"));
            editItem.setItemMeta(editMeta);
        }
        return editItem;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.GREEN + "Active Scenarios")) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta() || clickedItem.getItemMeta() == null) {
            return;
        }

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        if ("Edit Scenarios".equalsIgnoreCase(itemName) && player.hasPermission("nuhc.admin.editscenarios")) {
            new AddScenarioMenu().openMenu(player);
            return;
        }

        handleScenarioClick(player, itemName);
    }

    private void handleScenarioClick(Player player, String scenarioName) {
        Scenario scenario = NUHC.getInstance().getScenarioManager().getScenario(scenarioName);

        if (scenario != null) {
            if (player.hasPermission("nuhc.admin.removescenario")) {
                NUHC.getInstance().getGameConfig().removeScenario(scenario);
                player.sendMessage(ChatColor.RED + "Scenario " + scenario.getName() + " has been removed.");
                player.closeInventory();
            } else {
                player.sendMessage(ChatColor.GREEN + "Scenario: " + scenario.getName());
                player.sendMessage(ChatColor.GRAY + "Info: " + scenario.information());
            }
        }
    }
}
