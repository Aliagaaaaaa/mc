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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddScenarioMenu implements Listener {

    public void openMenu(Player player) {
        List<Scenario> availableScenarios = getAvailableScenarios();
        int size = Math.max(9, (int) Math.ceil(availableScenarios.size() / 9.0) * 9); // Asegurar al menos 9 slots
        Inventory menu = Bukkit.createInventory(null, size, ChatColor.GREEN + "Add Scenarios");

        for (Scenario scenario : availableScenarios) {
            ItemStack item = createScenarioItem(scenario);
            menu.addItem(item);
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

    private List<Scenario> getAvailableScenarios() {
        List<Scenario> allScenarios = NUHC.getInstance().getScenarioManager().getAllScenarios();
        List<Scenario> activeScenarios = NUHC.getInstance().getGameConfig().getScenarios();
        List<Scenario> availableScenarios = new ArrayList<>(allScenarios);

        // Eliminar los escenarios activos de la lista de disponibles
        availableScenarios.removeAll(activeScenarios);

        return availableScenarios;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!ChatColor.stripColor(event.getView().getTitle()).equals("Add Scenarios")) {
            return;
        }
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        String scenarioName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        Scenario scenario = NUHC.getInstance().getScenarioManager().getScenario(scenarioName);

        if (scenario != null) {
            // Añadir el escenario a los activos
            NUHC.getInstance().getGameConfig().getScenarios().add(scenario);
            player.sendMessage(ChatColor.GREEN + "Scenario " + scenario.getName() + " has been added!");
            player.closeInventory();
        }
    }
}
