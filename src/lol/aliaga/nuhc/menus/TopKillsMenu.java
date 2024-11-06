package lol.aliaga.nuhc.menus;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
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
        InventoryView view = event.getView();
        Inventory inventory = view.getTopInventory();

        // Verificar si el inventario es el del Top 10 Kills
        if (inventory.getTitle().equals("Top 10 Kills")) {
            // Cancelar cualquier intento de mover ítems
            event.setCancelled(true);
        }
    }


    public static void openTopKillsMenu(Player player) {
        // Crear un inventario con 10 espacios para el top 10
        Inventory menu = Bukkit.createInventory(null, 9 * 3, "Top 10 Kills");

        // Obtener todos los jugadores que tengan al menos 1 kill, ordenados de mayor a menor
        List<UHCPlayer> topPlayers = NUHC.getInstance().getUhcPlayerManager().getPlayers().stream()
                .filter(p -> p.getStats().getKills() > 0)
                .sorted(Comparator.comparingInt(p -> -p.getStats().getKills())) // Orden descendente
                .collect(Collectors.toList());

        // Verificar si no hay jugadores con kills
        if (topPlayers.isEmpty()) {
            // Mostrar datos aleatorios si nadie tiene al menos 1 kill
            Random random = new Random();
            List<Integer> randomKills = IntStream.range(0, 10)
                    .map(i -> random.nextInt(20) + 1) // Generar valores aleatorios entre 1 y 20
                    .boxed()
                    .sorted(Comparator.reverseOrder()) // Ordenar de mayor a menor
                    .collect(Collectors.toList());

            for (int i = 0; i < randomKills.size(); i++) {
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

                if (skullMeta != null) {
                    skullMeta.setDisplayName("Player" + (i + 1) + " - Kills: " + randomKills.get(i));
                    skull.setItemMeta(skullMeta);
                }

                // Colocar el ítem en el inventario
                menu.setItem(i, skull);
            }
        } else {
            // Asegurarnos de que solo tomamos los 10 primeros jugadores con al menos 1 kill
            List<UHCPlayer> top10Players = topPlayers.stream().limit(10).collect(Collectors.toList());

            // Agregar cada jugador al menú con su cabeza
            for (int i = 0; i < top10Players.size(); i++) {
                UHCPlayer uhcPlayer = top10Players.get(i);
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

                if (skullMeta != null) {
                    // Configurar el nombre y la cabeza del jugador
                    skullMeta.setOwner(Bukkit.getOfflinePlayer(uhcPlayer.getUniqueId()).getName());
                    skullMeta.setDisplayName(Bukkit.getOfflinePlayer(uhcPlayer.getUniqueId()).getName() + " - Kills: " + uhcPlayer.getStats().getKills());
                    skull.setItemMeta(skullMeta);
                }

                // Colocar el ítem en el inventario
                menu.setItem(i, skull);
            }
        }

        // Abrir el menú para el jugador
        player.openInventory(menu);
    }
}
