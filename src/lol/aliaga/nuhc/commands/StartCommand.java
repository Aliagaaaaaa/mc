package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameState;
import lol.aliaga.nuhc.scenarios.Scenario;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StartCommand implements CommandExecutor {

    private final Random random = new Random();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador o la consola.");
            return true;
        }

        int radio = NUHC.getInstance().getGameConfig().getBorder();
        World world = Bukkit.getWorld("world");
        if (world == null) {
            sender.sendMessage("No se pudo encontrar el mundo 'world'.");
            return true;
        }

        List<Player> jugadores = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<Location> ubicaciones = new ArrayList<>();
        for (int i = 0; i < jugadores.size(); i++) {
            Location loc = generarCoordenadaAleatoriaValida(world, radio);
            ubicaciones.add(loc);
        }

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                if (index >= jugadores.size()) {
                    iniciarCuentaRegresiva(jugadores);
                    cancel();
                    return;
                }

                Player jugador1 = jugadores.get(index);
                Player jugador2 = (index + 1 < jugadores.size()) ? jugadores.get(index + 1) : null;

                if (jugador1 != null) {
                    Location loc1 = ubicaciones.get(index);
                    jugador1.teleport(loc1);
                    jugador1.sendMessage("¡Has sido teletransportado!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Horse caballo1 = crearCaballoInvisible(world, loc1);
                            caballo1.setPassenger(jugador1);
                            jugador1.sendMessage("¡Ahora estás montado en un caballo invisible!");
                        }
                    }.runTaskLater(NUHC.getInstance(), 20L);
                }

                if (jugador2 != null) {
                    Location loc2 = ubicaciones.get(index + 1);
                    jugador2.teleport(loc2);
                    jugador2.sendMessage("¡Has sido teletransportado!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Horse caballo2 = crearCaballoInvisible(world, loc2);
                            caballo2.setPassenger(jugador2);
                            jugador2.sendMessage("¡Ahora estás montado en un caballo invisible!");
                        }
                    }.runTaskLater(NUHC.getInstance(), 20L);
                }

                index += 2;
            }
        }.runTaskTimer(NUHC.getInstance(), 0, 20);

        sender.sendMessage("¡Los jugadores están siendo teletransportados!");
        return true;
    }

    private void iniciarCuentaRegresiva(List<Player> jugadores) {
        NUHC.getInstance().setGameState(GameState.STARTING);
        new BukkitRunnable() {
            int countdown = 15;

            @Override
            public void run() {
                if (countdown > 0) {
                    Bukkit.broadcastMessage("La partida comienza en " + countdown + " segundo" + (countdown == 1 ? "" : "s") + "...");
                    countdown--;
                } else {
                    Bukkit.broadcastMessage("¡La partida ha comenzado!");

                    // Desmontar a todos los jugadores del caballo y eliminar el caballo
                    for (Player jugador : jugadores) {
                        if (jugador.isInsideVehicle() && jugador.getVehicle() instanceof Horse) {
                            Horse caballo = (Horse) jugador.getVehicle();
                            jugador.leaveVehicle(); // Bajar al jugador del caballo
                            caballo.remove(); // Eliminar el caballo
                        }
                    }

                    // Dar a los jugadores la cantidad específica de chuletas de vaca cocinadas
                    ItemStack chuletas = new ItemStack(Material.COOKED_BEEF, 10); // Cambia '10' por la cantidad deseada
                    for (Player jugador : jugadores) {
                        jugador.getInventory().addItem(chuletas);
                    }

                    iniciarPartida(); // Iniciar la partida
                    cancel();
                }
            }
        }.runTaskTimer(NUHC.getInstance(), 0, 20); // Cuenta regresiva cada segundo (20 ticks)
    }


    private void iniciarPartida() {
        Bukkit.broadcastMessage("¡La partida ha iniciado correctamente!");
        NUHC.getInstance().setGameState(GameState.IN_GAME);
        NUHC.getInstance().setStartTime(System.currentTimeMillis());
        NUHC.getInstance().setCurrentBorder(NUHC.getInstance().getGameConfig().getBorder());

        //registrar listeners de los scenarios
        for(Scenario scenario : NUHC.getInstance().getGameConfig().getScenarios()){
            NUHC.getInstance().getServer().getPluginManager().registerEvents(scenario, NUHC.getInstance());
        }

        updateBorder(2000);

        // Reemplazar scheduler.schedule con BukkitRunnable para activar PvP
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage("Activando pvp");
            }
        }.runTaskLater(NUHC.getInstance(), 20L * 60L * NUHC.getInstance().getGameConfig().getPvpTime()); // 20 ticks por segundo * 60 segundos por minuto * minutos

        // Reemplazar scheduler.schedule con BukkitRunnable para el Final Heal
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage("Final heal");
            }
        }.runTaskLater(NUHC.getInstance(), 20L * 60L * NUHC.getInstance().getGameConfig().getFinalHealTime()); // 20 ticks por segundo * 60 segundos por minuto * minutos

        reducirBorde(NUHC.getInstance().getCurrentBorder(), NUHC.getInstance().getGameConfig().getBorderShrinking());

    }

    private void updateBorder(int radius) {
        System.out.println(radius+"aaa");
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world set " + radius + " " + radius + " 0 0");
                final int blocksPerTick = 6;
                final int highestBlock = 4;

                int posX = radius;
                int negX = 0 - radius;

                int posZ = radius;
                int negZ = 0 - radius;

                final Queue<Location> locations1 = new ArrayDeque<>();
                final Queue<Location> locations2 = new ArrayDeque<>();

                final Queue<Location> locations3 = new ArrayDeque<>();
                final Queue<Location> locations4 = new ArrayDeque<>();

                final Queue<Location> locations5 = new ArrayDeque<>();
                final Queue<Location> locations6 = new ArrayDeque<>();

                final Queue<Location> locations7 = new ArrayDeque<>();
                final Queue<Location> locations8 = new ArrayDeque<>();



                for(int t = posX; t >= 0; t--)
                {
                    int min = Bukkit.getWorld("world").getHighestBlockYAt(t, posZ);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations1.add(new Location( Bukkit.getWorld("world"), t, y, posZ));
                        }
                    }
                }
                for(int t = negX; t <= 0; t++)
                {
                    int min =  Bukkit.getWorld("world").getHighestBlockYAt(t, posZ);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations2.add(new Location( Bukkit.getWorld("world"), t, y, posZ));
                        }
                    }
                }

                for(int t = posX; t >= 0; t--)
                {
                    int min = Bukkit.getWorld("world").getHighestBlockYAt(t, negZ);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations3.add(new Location(Bukkit.getWorld("world"), t, y, negZ));
                        }
                    }
                }
                for(int t = negX; t <= -0; t++)
                {
                    int min = Bukkit.getWorld("world").getHighestBlockYAt(t, negZ);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations4.add(new Location(Bukkit.getWorld("world"), t, y, negZ));
                        }
                    }
                }
                for(int t = posZ; t >= 0; t--)
                {
                    int min = Bukkit.getWorld("world").getHighestBlockYAt(posX, t);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations5.add(new Location(Bukkit.getWorld("world"), posX, y, t));
                        }
                    }
                }
                for(int t = negZ; t <= -0; t++)
                {
                    int min = Bukkit.getWorld("world").getHighestBlockYAt(posX, t);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations6.add(new Location(Bukkit.getWorld("world"), posX, y, t));
                        }
                    }
                }

                for(int t = posZ; t >= 0; t--)
                {
                    int min = Bukkit.getWorld("world").getHighestBlockYAt(negX, t);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations7.add(new Location(Bukkit.getWorld("world"), negX, y, t));
                        }
                    }
                }
                for(int t = negZ; t <= -0; t++)
                {
                    int min = Bukkit.getWorld("world").getHighestBlockYAt(negX, t);
                    int max = min + highestBlock;
                    if(max < 256)
                    {
                        for (int y = min; y < max; y++)
                        {
                            locations8.add(new Location(Bukkit.getWorld("world"), negX, y, t));
                        }
                    }
                }
                new BukkitRunnable()
                {

                    @Override
                    public void run()
                    {
                        for(int x = 0; x<blocksPerTick; x++)
                        {
                            if (!locations1.isEmpty())
                            {
                                locations1.poll().getBlock().setType(Material.BEDROCK);
                            }
                            if (!locations2.isEmpty())
                            {
                                locations2.poll().getBlock().setType(Material.BEDROCK);
                            }
                            if (!locations3.isEmpty())
                            {
                                locations3.poll().getBlock().setType(Material.BEDROCK);
                            }
                            if (!locations4.isEmpty())
                            {
                                locations4.poll().getBlock().setType(Material.BEDROCK);
                            }
                            if (!locations5.isEmpty())
                            {
                                locations5.poll().getBlock().setType(Material.BEDROCK);
                            }
                            if (!locations6.isEmpty())
                            {
                                locations6.poll().getBlock().setType(Material.BEDROCK);
                            }
                            if (!locations7.isEmpty())
                            {
                                locations7.poll().getBlock().setType(Material.BEDROCK);
                            }
                            if (!locations8.isEmpty())
                            {
                                locations8.poll().getBlock().setType(Material.BEDROCK);
                            }
                            else
                            {
                                this.cancel();
                            }
                        }
                    }
                }.runTaskTimer(NUHC.getInstance(), 0L, 1L);
            }
        }.runTask(NUHC.getInstance());
    }

    private void reducirBorde(int borde, int tiempo) {
        int[] bordes = {3000, 2000, 1500, 1000, 500, 250, 100, 50};
        int currentBorderIndex = IntStream.range(0, bordes.length).filter(i -> bordes[i] == borde).findFirst().orElse(-1);
        System.out.println(currentBorderIndex);

        if (currentBorderIndex == -1) {
            System.out.println("El borde no está en la lista de bordes permitidos.");
            return;
        }

        if (currentBorderIndex == bordes.length - 1) {
            System.out.println("El borde ya es el más pequeño.");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                int nuevoBorde = bordes[currentBorderIndex + 1];
                NUHC.getInstance().setCurrentBorder(bordes[currentBorderIndex + 1]);
                updateBorder(nuevoBorde);
                System.out.println("Borde reducido a: " + nuevoBorde);

                reducirBorde(nuevoBorde, 1);
            }
        }.runTaskLater(NUHC.getInstance(), 20L * 60L * tiempo); // 20 ticks por segundo * 60 segundos por minuto * minutos
    }


    private Location generarCoordenadaAleatoriaValida(World world, int radio) {
        Location loc;
        int intentos = 0;
        int maxIntentos = 100; // Evita posibles bucles infinitos

        do {
            loc = generarCoordenadaAleatoria(world, radio);
            intentos++;
            if (intentos > maxIntentos) {
                loc = world.getSpawnLocation();
                break;
            }
        } while (!esUbicacionValida(world, loc));

        return loc;
    }

    private Location generarCoordenadaAleatoria(World world, int radio) {
        int x = random.nextInt(2 * radio + 1) - radio;
        int z = random.nextInt(2 * radio + 1) - radio;
        int y = world.getHighestBlockYAt(x, z); // Asegurarse de teletransportar sobre el suelo
        return new Location(world, x, y, z);
    }

    private boolean esUbicacionValida(World world, Location loc) {
        Material material = loc.getBlock().getType();
        Material bloqueDebajo = loc.clone().subtract(0, 1, 0).getBlock().getType();

        boolean noEnAgua = !material.equals(Material.WATER) && !material.equals(Material.STATIONARY_WATER);
        boolean noSobreAgua = !bloqueDebajo.equals(Material.WATER) && !bloqueDebajo.equals(Material.STATIONARY_WATER);
        boolean noEnLava = !material.equals(Material.LAVA) && !material.equals(Material.STATIONARY_LAVA);
        boolean noSobreLava = !bloqueDebajo.equals(Material.LAVA) && !bloqueDebajo.equals(Material.STATIONARY_LAVA);
        boolean noSobreArbol = !(bloqueDebajo.equals(Material.LEAVES) || bloqueDebajo.equals(Material.LEAVES_2));

        boolean sinAguaAlrededor = true;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location bloqueAlrededor = loc.clone().add(x, 0, z);
                Material materialAlrededor = bloqueAlrededor.getBlock().getType();
                if (materialAlrededor.equals(Material.WATER) || materialAlrededor.equals(Material.STATIONARY_WATER)) {
                    sinAguaAlrededor = false;
                    break;
                }
            }
            if (!sinAguaAlrededor) {
                break;
            }
        }

        return noEnAgua && noSobreAgua && noEnLava && noSobreLava && noSobreArbol && sinAguaAlrededor;
    }


    private Horse crearCaballoInvisible(World world, Location location) {
        Horse horse = (Horse) world.spawnEntity(location.add(0.5, 1, 0.5), EntityType.HORSE);
        horse.setMaxHealth(20);
        horse.setHealth(20);
        horse.setAdult();
        horse.setTamed(true);

        try {
            net.minecraft.server.v1_8_R3.Entity horseEntity = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftHorse) horse).getHandle();

            Field invulnerableField = net.minecraft.server.v1_8_R3.Entity.class.getDeclaredField("invulnerable");
            invulnerableField.setAccessible(true);
            invulnerableField.setBoolean(horseEntity, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) horse).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1); // Desactiva la IA del caballo
        tag.setInt("Silent", 1); // Hace al caballo silencioso
        nmsEntity.f(tag);

        // Asegurar que el caballo sea invisible
        new BukkitRunnable() {
            @Override
            public void run() {
                ((CraftEntity) horse).getHandle().setInvisible(true);
            }
        }.runTaskLater(NUHC.getInstance(), 1L); // Retraso de 1 tick

        return horse;
    }

}