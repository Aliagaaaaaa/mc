package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameState;
import lol.aliaga.nuhc.player.UHCPlayerState;
import lol.aliaga.nuhc.scenarios.Scenario;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.IntStream;

public class StartCommand implements CommandExecutor {

    private final Random random = new Random();
    public static long seconds = 0;
    public static BukkitRunnable timeTask;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "Este comando solo puede ser ejecutado por un jugador o la consola.");
            return true;
        }

        int radio = NUHC.getInstance().getGameConfig().getBorder();
        World world = Bukkit.getWorld("world");
        if (world == null) {
            sender.sendMessage(ChatColor.RED + "No se pudo encontrar el mundo 'world'.");
            return true;
        }

        setInitialBorder(world);
        List<Player> jugadores = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<Location> ubicaciones = generatePlayerLocations(world, jugadores.size(), radio);

        teleportPlayersWithCountdown(jugadores, ubicaciones);
        sender.sendMessage(ChatColor.GREEN + "¡Los jugadores están siendo teletransportados!");
        return true;
    }

    private void setInitialBorder(World world) {
        int borderSize = NUHC.getInstance().getGameConfig().getBorder();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb world set " + borderSize + " " + borderSize + " 0 0");
    }

    private List<Location> generatePlayerLocations(World world, int playerCount, int radius) {
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            locations.add(generateValidRandomLocation(world, radius));
        }
        return locations;
    }

    private Location generateValidRandomLocation(World world, int radius) {
        Location loc;
        int attempts = 0;
        do {
            loc = new Location(world, random.nextInt(2 * radius + 1) - radius, 0, random.nextInt(2 * radius + 1) - radius);
            loc.setY(world.getHighestBlockYAt(loc));
            attempts++;
            if (attempts > 100) {
                return world.getSpawnLocation();
            }
        } while (!isLocationValid(loc));
        return loc;
    }

    private boolean isLocationValid(Location loc) {
        Material ground = loc.getBlock().getType();
        Material belowGround = loc.clone().subtract(0, 1, 0).getBlock().getType();

        boolean isSolidGround = belowGround.isSolid();
        boolean isNotLiquid = !ground.equals(Material.WATER) && !ground.equals(Material.LAVA) && !belowGround.equals(Material.WATER) && !belowGround.equals(Material.LAVA);

        return isSolidGround && isNotLiquid;
    }

    private void teleportPlayersWithCountdown(List<Player> players, List<Location> locations) {
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                if (index >= players.size()) {
                    startCountdown(players);
                    cancel();
                    return;
                }

                teleportPlayerWithHorse(players.get(index), locations.get(index));
                if (index + 1 < players.size()) {
                    teleportPlayerWithHorse(players.get(index + 1), locations.get(index + 1));
                }
                index += 2;
            }
        }.runTaskTimer(NUHC.getInstance(), 0, 20);
    }

    private void teleportPlayerWithHorse(Player player, Location location) {
        player.teleport(location);
        player.sendMessage(ChatColor.GREEN + "¡Has sido teletransportado!");

        new BukkitRunnable() {
            @Override
            public void run() {
                Horse horse = spawnInvisibleHorse(location.getWorld(), location);
                horse.setPassenger(player);
                player.sendMessage(ChatColor.GREEN + "¡Ahora estás montado en un caballo invisible!");
            }
        }.runTaskLater(NUHC.getInstance(), 20L);
    }

    private Horse spawnInvisibleHorse(World world, Location location) {
        Horse horse = (Horse) world.spawnEntity(location.add(0.5, 1, 0.5), EntityType.HORSE);
        horse.setAdult();
        horse.setTamed(true);
        horse.setMaxHealth(20);
        horse.setHealth(20);
        setHorseAttributes(horse);
        return horse;
    }

    private void setHorseAttributes(Horse horse) {
        try {
            net.minecraft.server.v1_8_R3.Entity nmsHorse = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftHorse) horse).getHandle();
            Field invulnerableField = net.minecraft.server.v1_8_R3.Entity.class.getDeclaredField("invulnerable");
            invulnerableField.setAccessible(true);
            invulnerableField.setBoolean(nmsHorse, true);

            NBTTagCompound tag = nmsHorse.getNBTTag() != null ? nmsHorse.getNBTTag() : new NBTTagCompound();
            nmsHorse.c(tag);
            tag.setInt("NoAI", 1);
            tag.setInt("Silent", 1);
            nmsHorse.f(tag);

            new BukkitRunnable() {
                @Override
                public void run() {
                    nmsHorse.setInvisible(true);
                }
            }.runTaskLater(NUHC.getInstance(), 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBorder(int radius) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world set " + radius + " " + radius + " 0 0");
                final int blocksPerTick = 6;
                final int highestBlock = 4;

                int posX = radius;
                int negX = -radius;
                int posZ = radius;
                int negZ = -radius;

                final Queue<Location> locations = new ArrayDeque<>();

                // Generar los bloques de bedrock en los bordes del cuadrado
                for (int t = posX; t >= negX; t--) {
                    int maxHeight1 = Bukkit.getWorld("world").getHighestBlockYAt(t, posZ) + highestBlock;
                    int maxHeight2 = Bukkit.getWorld("world").getHighestBlockYAt(t, negZ) + highestBlock;

                    for (int y = Bukkit.getWorld("world").getHighestBlockYAt(t, posZ); y < maxHeight1; y++) {
                        locations.add(new Location(Bukkit.getWorld("world"), t, y, posZ));
                    }

                    for (int y = Bukkit.getWorld("world").getHighestBlockYAt(t, negZ); y < maxHeight2; y++) {
                        locations.add(new Location(Bukkit.getWorld("world"), t, y, negZ));
                    }
                }

                for (int t = posZ; t >= negZ; t--) {
                    int maxHeight1 = Bukkit.getWorld("world").getHighestBlockYAt(posX, t) + highestBlock;
                    int maxHeight2 = Bukkit.getWorld("world").getHighestBlockYAt(negX, t) + highestBlock;

                    for (int y = Bukkit.getWorld("world").getHighestBlockYAt(posX, t); y < maxHeight1; y++) {
                        locations.add(new Location(Bukkit.getWorld("world"), posX, y, t));
                    }

                    for (int y = Bukkit.getWorld("world").getHighestBlockYAt(negX, t); y < maxHeight2; y++) {
                        locations.add(new Location(Bukkit.getWorld("world"), negX, y, t));
                    }
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < blocksPerTick; i++) {
                            if (!locations.isEmpty()) {
                                Location loc = locations.poll();
                                loc.getBlock().setType(Material.BEDROCK);
                            } else {
                                cancel();
                                break;
                            }
                        }
                    }
                }.runTaskTimer(NUHC.getInstance(), 0L, 1L);
            }
        }.runTask(NUHC.getInstance());
    }


    private void startCountdown(List<Player> players) {
        NUHC.getInstance().setGameState(GameState.STARTING);
        new BukkitRunnable() {
            int countdown = 15;

            @Override
            public void run() {
                if (countdown > 0) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "La partida comienza en " + countdown + " segundo" + (countdown == 1 ? "" : "s") + "...");
                    countdown--;
                } else {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "¡La partida ha comenzado!");
                    dismountPlayers(players);
                    giveStartItems(players);
                    startGame();
                    cancel();
                }
            }
        }.runTaskTimer(NUHC.getInstance(), 0, 20);
    }

    private void dismountPlayers(List<Player> players) {
        for (Player player : players) {
            if (player.isInsideVehicle() && player.getVehicle() instanceof Horse) {
                Horse horse = (Horse) player.getVehicle();
                player.leaveVehicle();
                horse.remove();
            }
        }
    }

    private void giveStartItems(List<Player> players) {
        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 10);
        for (Player player : players) {
            player.getInventory().addItem(steak);
        }
    }

    private void startGame() {
        NUHC.getInstance().setGameState(GameState.IN_GAME);
        NUHC.getInstance().setStartTime(System.currentTimeMillis());
        NUHC.getInstance().setCurrentBorder(NUHC.getInstance().getGameConfig().getBorder());

        Bukkit.broadcastMessage(ChatColor.GREEN + "¡La partida ha iniciado correctamente!");

        for (Scenario scenario : NUHC.getInstance().getGameConfig().getScenarios()) {
            NUHC.getInstance().getServer().getPluginManager().registerEvents(scenario, NUHC.getInstance());
        }

        startTimeTask();
    }

    public void startTimeTask() {
        if (timeTask != null) {
            timeTask.cancel();
        }

        final int[] borders = {3000, 2000, 1500, 1000, 500, 250, 100, 50};
        int initialBorder = NUHC.getInstance().getGameConfig().getBorder();

        final int[] currentBorderIndex = {IntStream.range(0, borders.length)
                .filter(i -> borders[i] == initialBorder)
                .findFirst()
                .orElse(0) + 1};

        int borderShrinkingTime = NUHC.getInstance().getGameConfig().getBorderShrinking() * 60;
        final int[] nextBorderTime = {borderShrinkingTime};

        timeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (NUHC.getInstance().getGameState() == GameState.IN_GAME) {
                    seconds++;

                    int pvpTime = NUHC.getInstance().getGameConfig().getPvpTime() * 60;
                    if (isCountdownTime(pvpTime)) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "PvP comienza en " + formatCountdown((int) (pvpTime - seconds)) + ".");
                    }
                    if (seconds == pvpTime) enablePvP();

                    int finalHealTime = NUHC.getInstance().getGameConfig().getFinalHealTime() * 60;
                    if (isCountdownTime(finalHealTime)) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "El Final Heal comienza en " + formatCountdown((int) (finalHealTime - seconds)) + ".");
                    }
                    if (seconds == finalHealTime) startFinalHeal();

                    if (isCountdownTime(nextBorderTime[0])) {
                        Bukkit.broadcastMessage(ChatColor.RED + "El borde se reducirá en " + formatCountdown((int) (nextBorderTime[0] - seconds)) + ".");
                    }
                    if (seconds >= nextBorderTime[0] && currentBorderIndex[0] < borders.length) {
                        int newBorder = borders[currentBorderIndex[0]++];
                        nextBorderTime[0] += 300;
                        shrinkBorder(newBorder);
                    }
                } else {
                    cancel();
                    timeTask = null;
                    seconds = 0;
                }
            }
        };
        timeTask.runTaskTimer(NUHC.getInstance(), 0L, 20L);
    }

    private boolean isCountdownTime(int targetTime) {
        return seconds == targetTime - 15 || seconds == targetTime - 10 || seconds == targetTime - 5 || (seconds <= targetTime - 1 && seconds > targetTime - 15);
    }

    private String formatCountdown(int countdown) {
        return countdown + " segundo" + (countdown == 1 ? "" : "s");
    }

    private void enablePvP() {
        Bukkit.getScheduler().runTaskLater(NUHC.getInstance(), () -> {
            Bukkit.broadcastMessage(ChatColor.GREEN + "¡PvP está activado!");
            World world = Bukkit.getWorld("world");
            if (world != null) {
                world.setPVP(true);
            }
        }, 20L);
    }

    private void startFinalHeal() {
        Bukkit.getScheduler().runTaskLater(NUHC.getInstance(), () -> {
            Bukkit.broadcastMessage(ChatColor.GREEN + "¡Final Heal ha comenzado!");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId()).getState() == UHCPlayerState.PLAYER) {
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setFireTicks(0);
                }
            }
        }, 20L);
    }

    private void shrinkBorder(int newBorder) {
        Bukkit.getScheduler().runTask(NUHC.getInstance(), () -> {
            Bukkit.broadcastMessage(ChatColor.RED + "El borde se ha reducido a " + newBorder + " bloques.");
            updateBorder(newBorder);
            setBorder(newBorder);
        });
    }

    private void setBorder(int newBorder) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb world set " + newBorder + " " + newBorder + " 0 0");
        NUHC.getInstance().setCurrentBorder(newBorder);
    }
}
