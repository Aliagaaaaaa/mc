package lol.aliaga.nuhc.listeners;

import lol.aliaga.nuhc.NUHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NUHC.getInstance().getUhcPlayerManager().createPlayer(player.getUniqueId());
        player.teleport(new Location(Bukkit.getWorld("lobby"), 0,Bukkit.getWorld("lobby").getHighestBlockYAt(0,0),0));

        Bukkit.broadcastMessage(player.getName() + " joined the world");
    }
}
