package lol.aliaga.nuhc.listeners;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayer;
import lol.aliaga.nuhc.player.UHCPlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpectatorListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if (uhcPlayer.getState() != UHCPlayerState.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if (uhcPlayer.getState() != UHCPlayerState.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if (uhcPlayer.getState() != UHCPlayerState.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(event.getEntity().getUniqueId());
            if (uhcPlayer.getState() != UHCPlayerState.PLAYER) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(event.getDamager().getUniqueId());
            if (uhcPlayer.getState() != UHCPlayerState.PLAYER) {
                event.setCancelled(true);
            }
        }
    }
}
