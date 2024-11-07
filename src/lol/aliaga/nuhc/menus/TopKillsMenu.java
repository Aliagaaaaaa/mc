package lol.aliaga.nuhc.menus;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayer;
import lol.aliaga.nuhc.player.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TopKillsMenu implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (ChatColor.stripColor(inventory.getTitle()).equals("Top 10 Kills")) {
            event.setCancelled(true);
        }
    }

    public static void openTopKillsMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "Top 10 Kills");

        List<UHCPlayer> topPlayers = NUHC.getInstance().getUhcPlayerManager().getPlayers().stream()
                .filter(p -> p.getStats().getStat(StatType.KILLS) > 0)
                .sorted(Comparator.comparingInt(p -> -p.getStats().getStat(StatType.KILLS)))
                .limit(10)
                .collect(Collectors.toList());

        if (topPlayers.isEmpty()) {
            fillWithRandomKills(menu);
        } else {
            fillWithTopPlayers(menu, topPlayers);
        }

        player.openInventory(menu);
    }

    // Método para rellenar el menú con los 10 jugadores principales
    private static void fillWithTopPlayers(Inventory menu, List<UHCPlayer> topPlayers) {
        for (int i = 0; i < topPlayers.size(); i++) {
            UHCPlayer uhcPlayer = topPlayers.get(i);
            ItemStack skull = createPlayerSkull(Bukkit.getOfflinePlayer(uhcPlayer.getUniqueId()).getName(), uhcPlayer.getStats().getStat(StatType.KILLS));
            menu.setItem(i, skull);
        }
    }

    // Método para rellenar el menú con valores de kills aleatorios si no hay jugadores con kills
    private static void fillWithRandomKills(Inventory menu) {
        Random random = new Random();
        List<Integer> randomKills = IntStream.range(0, 10)
                .map(i -> random.nextInt(20) + 1)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        for (int i = 0; i < randomKills.size(); i++) {
            ItemStack skull = createPlayerSkull("Player" + (i + 1), randomKills.get(i));
            menu.setItem(i, skull);
        }
    }

    // Crea un ítem de cabeza de jugador con la cantidad de kills
    private static ItemStack createPlayerSkull(String playerName, int kills) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwner(playerName);
            skullMeta.setDisplayName(ChatColor.GOLD + playerName + ChatColor.WHITE + " - " + ChatColor.RED + "Kills: " + kills);
            skull.setItemMeta(skullMeta);
        }
        return skull;
    }
}
