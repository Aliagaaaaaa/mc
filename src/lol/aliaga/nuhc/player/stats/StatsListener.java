package lol.aliaga.nuhc.player.stats;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class StatsListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());

        switch (blockType) {
            case DIAMOND_ORE:
                uhcPlayer.getStats().addMiningEvent("Diamond", 1);
                break;
            case GOLD_ORE:
                uhcPlayer.getStats().addMiningEvent("Gold", 1);
                break;
            case IRON_ORE:
                uhcPlayer.getStats().addMiningEvent("Iron", 1);
                break;
            case LAPIS_ORE:
                uhcPlayer.getStats().addMiningEvent("Lapis Lazuli", 1);
                break;
            case QUARTZ_ORE:
                uhcPlayer.getStats().addMiningEvent("Quartz", 1);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player target = (Player) event.getEntity();

            UHCPlayer uhcDamager = NUHC.getInstance().getUhcPlayerManager().getPlayer(damager.getUniqueId());
            UHCPlayer uhcTarget = NUHC.getInstance().getUhcPlayerManager().getPlayer(target.getUniqueId());

            uhcDamager.getStats().addDamageDealt(target.getName(), event.getFinalDamage());
            uhcTarget.getStats().addDamageTaken(damager.getName(), event.getFinalDamage());
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            Player player = (Player) event.getEntity();
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
            uhcPlayer.getStats().addArrowShot();
        }
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                UHCPlayer uhcShooter = NUHC.getInstance().getUhcPlayerManager().getPlayer(shooter.getUniqueId());
                uhcShooter.getStats().addArrowHit();
            }
        }
    }

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());

        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            if (event.getItem().getItemMeta().hasDisplayName() &&
                    "GoldenHead".equalsIgnoreCase(event.getItem().getItemMeta().getDisplayName())) {
                uhcPlayer.getStats().addGoldenHeadConsumed();
            } else {
                uhcPlayer.getStats().addGoldenAppleConsumed();
            }
        }
    }

    @EventHandler
    public void onPlayerMount(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof Horse) {
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
            uhcPlayer.getStats().addHorseRiding();
        }
    }
}
