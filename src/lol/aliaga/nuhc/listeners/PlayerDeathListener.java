package lol.aliaga.nuhc.listeners;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayer;
import lol.aliaga.nuhc.player.UHCPlayerState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());

        if (uhcPlayer != null) {
            // Guardar la ubicación de muerte
            uhcPlayer.setDeathLocation(player.getLocation());

            // Cambiar el estado del jugador a SPECTATOR_DEATH y activar el modo espectador
            uhcPlayer.setState(UHCPlayerState.SPECTATOR_DEATH);
            uhcPlayer.setSpectatorMode();
            player.sendMessage(ChatColor.RED + "You have died and are now a spectator.");
        }
    }
}