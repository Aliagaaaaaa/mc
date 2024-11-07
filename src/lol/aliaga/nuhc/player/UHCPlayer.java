package lol.aliaga.nuhc.player;

import lol.aliaga.nuhc.player.stats.StatType;
import lol.aliaga.nuhc.player.stats.UHCStats;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class UHCPlayer {
    @Getter
    private final UUID uniqueId;
    @Getter
    private final UHCStats stats;
    @Getter
    @Setter
    private UHCPlayerState state;
    private ItemStack[] savedInventory;
    private ItemStack[] savedArmor;
    @Setter
    private Location deathLocation; // Coordenadas de la muerte del jugador

    public UHCPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.state = UHCPlayerState.PLAYER;
        this.stats = new UHCStats();
    }

    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) {
            player.sendMessage(message);
        }
    }

    public void setSpectatorMode() {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) {
            // Guardar el inventario, armadura y la ubicación de muerte
            savedInventory = player.getInventory().getContents();
            savedArmor = player.getInventory().getArmorContents();
            deathLocation = player.getLocation();

            // Limpiar inventario y cambiar a modo espectador
            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);

            // Ocultar al jugador de todos los demás
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                if (!otherPlayer.getUniqueId().equals(uniqueId)) {
                    otherPlayer.hidePlayer(player);
                }
            }

            player.sendMessage(ChatColor.YELLOW + "You are now in spectator mode and can fly.");
        }
    }

    public void restorePlayerFromSpectator() {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null && savedInventory != null && savedArmor != null && deathLocation != null) {
            player.getInventory().setContents(savedInventory);
            player.getInventory().setArmorContents(savedArmor);
            player.teleport(deathLocation);
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);

            // Mostrar al jugador a todos los demás
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                if (!otherPlayer.getUniqueId().equals(uniqueId)) {
                    otherPlayer.showPlayer(player);
                }
            }

            player.sendMessage(ChatColor.GREEN + "You have been restored to your death location.");
        }
    }
}
