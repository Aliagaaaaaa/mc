package lol.aliaga.nuhc.player.stats;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class StatsListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
        Material blockType = event.getBlock().getType();

        switch (blockType) {
            case DIAMOND_ORE:
                uhcPlayer.getStats().incrementStat(StatType.MINING_DIAMOND, 1);
                uhcPlayer.getStats().recordAction("Mining", "Diamond", 1);
                break;
            case GOLD_ORE:
                uhcPlayer.getStats().incrementStat(StatType.MINING_GOLD, 1);
                uhcPlayer.getStats().recordAction("Mining", "Gold", 1);
                break;
            case IRON_ORE:
                uhcPlayer.getStats().incrementStat(StatType.MINING_IRON, 1);
                uhcPlayer.getStats().recordAction("Mining", "Iron", 1);
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

            uhcDamager.getStats().incrementStat(StatType.DAMAGE_DEALT, (int) event.getFinalDamage());
            uhcDamager.getStats().recordAction("Damage", "Damage to " + target.getName(), (int) event.getFinalDamage());

            uhcTarget.getStats().incrementStat(StatType.DAMAGE_TAKEN, (int) event.getFinalDamage());
            uhcTarget.getStats().recordAction("Damage", "Damage received from " + damager.getName(), (int) event.getFinalDamage());
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
            uhcPlayer.getStats().incrementStat(StatType.ARROWS_SHOT, 1);
        }
    }

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
        ItemStack item = event.getItem();

        if (item.getType() == Material.GOLDEN_APPLE) {
            if (item.getItemMeta().hasDisplayName() && "GoldenHead".equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
                uhcPlayer.getStats().incrementStat(StatType.GOLDEN_HEADS_CONSUMED, 1);
            } else {
                uhcPlayer.getStats().incrementStat(StatType.GOLDEN_APPLES_CONSUMED, 1);
            }
        }
    }

    @EventHandler
    public void onPlayerMount(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Horse) {
            Player player = event.getPlayer();
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
            uhcPlayer.getStats().incrementStat(StatType.HORSES_RIDDEN, 1);
            uhcPlayer.getStats().recordAction("Mount", "Horse ridden", 1);
        }
    }
}
