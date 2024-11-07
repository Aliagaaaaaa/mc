package lol.aliaga.nuhc.listeners;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameState;
import lol.aliaga.nuhc.player.UHCPlayer;
import lol.aliaga.nuhc.player.UHCPlayerManager;
import lol.aliaga.nuhc.player.UHCPlayerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class LobbyListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(NUHC.getInstance().getGameState() == GameState.WAITING_START || NUHC.getInstance().getGameState() == GameState.SETUP  || NUHC.getInstance().getGameState() == GameState.STARTING){
            Player player = event.getPlayer();
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());

            Location lobbyLocation = new Location(Bukkit.getWorld("lobby"), 0, Bukkit.getWorld("lobby").getHighestBlockYAt(0, 0), 0);
            player.teleport(lobbyLocation);
            player.getInventory().clear();
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setGameMode(GameMode.SURVIVAL);

            // Verificar si el jugador está en modo espectador
            if (uhcPlayer.getState() != UHCPlayerState.PLAYER) {
                setSpectatorMode(player);
            } else {
                player.sendMessage(ChatColor.GREEN + "Welcome to the game! Prepare for the challenge.");
            }
        }
    }

    private void setSpectatorMode(Player player) {
        // Configurar modo espectador con vuelo activado
        player.setGameMode(GameMode.SPECTATOR);
        player.setAllowFlight(true);
        player.setFlying(true);

        // Ocultar al jugador de otros jugadores
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (!otherPlayer.getUniqueId().equals(player.getUniqueId())) {
                otherPlayer.hidePlayer(player);
            }
        }
        player.sendMessage(ChatColor.YELLOW + "You are in spectator mode and can fly around.");
    }



    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Opción para manejar cuando un jugador sale, si es necesario.
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (isInLobby(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot break blocks in the lobby.");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (isInLobby(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot place blocks in the lobby.");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isInLobby(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Interactions are disabled in the lobby.");
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isInLobby(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isInLobby(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (isInLobby(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot drop items in the lobby.");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (isInLobby(player)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Inventory manipulation is disabled in the lobby.");
            }
        }
    }

    private void resetPlayerToLobbyDefaults(Player player) {
        // Configuración inicial del jugador en el lobby
        player.getInventory().clear();
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
    }

    private boolean isInLobby(Player player) {
        // Verifica si el jugador está en el lobby (puedes ajustar esta lógica según sea necesario)
        return player.getWorld().getName().equalsIgnoreCase("lobby");
    }
}
