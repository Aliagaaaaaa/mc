package lol.aliaga.nuhc.scenarios;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.Iterator;
import java.util.Random;

public class Timebomb implements Scenario {

    private DoubleChestInventory lastDeathInventory;
    private Random random = new Random();

    @Override
    public String getName() {
        return "Timebomb";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "tb";
    }

    @Override
    public Material getIcon() {
        return Material.TNT;
    }

    @Override
    public void unregister() {

    }

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (NUHC.getInstance().getGameState() != GameState.IN_GAME) return;

        Player player = event.getEntity().getPlayer();

        Location firstChestLocation = player.getLocation();
        firstChestLocation.getBlock().setType(Material.CHEST);

        Location secondChestLocation = firstChestLocation.clone().add(1, 0, 0); // El segundo cofre
        secondChestLocation.getBlock().setType(Material.CHEST);

        firstChestLocation.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
        secondChestLocation.clone().add(0, 1, 0).getBlock().setType(Material.AIR);

        // Poner items en el cofre
        Chest chest = (Chest) firstChestLocation.getBlock().getState();
        if (chest.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
            DoubleChestInventory doubleChestInventory = (DoubleChestInventory) doubleChest.getInventory();

            for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    continue;
                }

                doubleChestInventory.addItem(itemStack);
            }

            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    continue;
                }

                doubleChestInventory.addItem(itemStack);
            }

            this.lastDeathInventory = doubleChestInventory; // Store for later use
        }

        player.getWorld().spawn(player.getLocation(), ExperienceOrb.class).setExperience(player.getLevel() * 7);

        Location middleLocation = firstChestLocation.clone().add(secondChestLocation).multiply(0.5).add(0.1, 0.7, 0);

        ArmorStand hologram = createHologram(middleLocation, "30");

        new TimeBombXD(player.getName(), player.getLocation(), hologram).runTaskTimer(NUHC.getInstance(), 0L, 20L); // Actualizar cada segundo

        event.getDrops().clear();
    }


    @EventHandler
    public void onPlayerDropItemEvent(PlayerDeathEvent event) {
        event.getDrops().clear();
        event.getEntity().teleport(event.getEntity().getLocation());
        event.getEntity().setHealth(20);
    }

    @EventHandler
    public void onBedrockExplode(EntityExplodeEvent event) {
        Iterator<Block> it = event.blockList().iterator();
        while (it.hasNext()) {
            Block block = it.next();
            if (block.getType() == Material.BEDROCK) {
                it.remove();
            }
        }
    }

    private class TimeBombXD extends BukkitRunnable {

        private Location location;
        private String name;
        private ArmorStand hologram;
        private int countdown = 30;

        public TimeBombXD(String name, Location location, ArmorStand hologram) {
            this.name = name;
            this.location = location;
            this.hologram = hologram;
        }

        @Override
        public void run() {
            if (countdown <= 0) {
                // Eliminar holograma y detonar bomba
                hologram.remove();
                this.location.getWorld().spigot().strikeLightning(this.location, true);
                this.location.getWorld().createExplosion(this.location, 10f);
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.WHITE + "TimeBomb" + ChatColor.DARK_RED + "] " + ChatColor.WHITE + this.name + "'s corpse has exploded!");
                this.cancel();
            } else {
                // Actualizar holograma con el tiempo restante
                hologram.setCustomName(ChatColor.GREEN + "" + countdown);
                Bukkit.broadcastMessage(String.valueOf(countdown));
                countdown--;
            }
        }
    }

    // Método para crear un holograma (ArmorStand)
    private ArmorStand createHologram(Location location, String text) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setCustomName(text); // Texto del holograma
        armorStand.setCustomNameVisible(true); // Visible siempre
        armorStand.setGravity(false); // No se cae
        armorStand.setVisible(false); // No mostrar el armor stand
        armorStand.setMarker(true); // No colisionar con jugadores o entidades
        armorStand.setBasePlate(false); // Sin placa base
        armorStand.setArms(false); // Sin brazos
        return armorStand;
    }
}
