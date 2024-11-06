package lol.aliaga.nuhc.menus;

import lol.aliaga.nuhc.NUHC;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Random;

public class WorldMenu implements Listener {

    public static void openWorldMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, ChatColor.GREEN + "World Management");

        // Opción para cargar chunks
        ItemStack loadChunks = new ItemStack(Material.GRASS);
        ItemMeta loadChunksMeta = loadChunks.getItemMeta();
        loadChunksMeta.setDisplayName(ChatColor.GREEN + "Load Chunks");
        loadChunks.setItemMeta(loadChunksMeta);
        menu.setItem(0, loadChunks);

        // Opción para eliminar mundo
        ItemStack deleteWorld = new ItemStack(Material.TNT);
        ItemMeta deleteWorldMeta = deleteWorld.getItemMeta();
        deleteWorldMeta.setDisplayName(ChatColor.RED + "Delete World");
        deleteWorld.setItemMeta(deleteWorldMeta);
        menu.setItem(1, deleteWorld);

        // Opción para crear mundo
        ItemStack createWorld = new ItemStack(Material.DIAMOND);
        ItemMeta createWorldMeta = createWorld.getItemMeta();
        createWorldMeta.setDisplayName(ChatColor.BLUE + "Create World");
        createWorld.setItemMeta(createWorldMeta);
        menu.setItem(2, createWorld);

        // Opción para teletransportar al mundo
        ItemStack teleportWorld = new ItemStack(Material.ENDER_PEARL);
        ItemMeta teleportWorldMeta = teleportWorld.getItemMeta();
        teleportWorldMeta.setDisplayName(ChatColor.YELLOW + "Teleport to World");
        teleportWorld.setItemMeta(teleportWorldMeta);
        menu.setItem(3, teleportWorld);

        // Opción para crear y cargar chunks del mundo Nether si está habilitado en la configuración
        if (NUHC.getInstance().getGameConfig().isNether()) {
            // Botón para crear mundo de Nether
            ItemStack createNetherWorld = new ItemStack(Material.NETHER_STAR);
            ItemMeta createNetherWorldMeta = createNetherWorld.getItemMeta();
            createNetherWorldMeta.setDisplayName(ChatColor.DARK_RED + "Create Nether World");
            createNetherWorld.setItemMeta(createNetherWorldMeta);
            menu.setItem(4, createNetherWorld);

            // Botón para cargar chunks del Nether
            ItemStack loadNetherChunks = new ItemStack(Material.BLAZE_POWDER); // Cambiado a BLAZE_POWDER
            ItemMeta loadNetherChunksMeta = loadNetherChunks.getItemMeta();
            loadNetherChunksMeta.setDisplayName(ChatColor.DARK_RED + "Load Nether Chunks");
            loadNetherChunks.setItemMeta(loadNetherChunksMeta);
            menu.setItem(5, loadNetherChunks);

            // Botón para teletransportar al Nether
            ItemStack teleportNether = new ItemStack(Material.ENDER_PEARL);
            ItemMeta teleportNetherMeta = teleportNether.getItemMeta();
            teleportNetherMeta.setDisplayName(ChatColor.RED + "Teleport to Random Nether");
            teleportNether.setItemMeta(teleportNetherMeta);
            menu.setItem(6, teleportNether);
        }

        // Abrir el menú al jugador
        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!ChatColor.stripColor(event.getView().getTitle()).equals("World Management")) {
            return;
        }

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        switch (itemName) {
            case "Load Chunks":
                loadChunks(player);
                break;
            case "Delete World":
                deleteWorld(player);
                break;
            case "Create World":
                createWorld(player);
                break;
            case "Teleport to World":
                teleportToWorld(player);
                break;
            case "Create Nether World":
                createNetherWorld(player);
                break;
            case "Load Nether Chunks":
                loadNetherChunks(player);
                break;
            case "Teleport to Random Nether":
                teleportToRandomNether(player);
                break;
            default:
                break;
        }

        player.closeInventory();
    }

    public static void loadChunks(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world set " + NUHC.getInstance().getGameConfig().getBorder() + " " + NUHC.getInstance().getGameConfig().getBorder() + " 0 0");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world fill 125");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world fill confirm");
        player.sendMessage(ChatColor.GREEN + "Chunks are being loaded.");
    }

    public static void createWorld(Player player) {
        WorldCreator worldCreator = new WorldCreator("world");
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.type(WorldType.NORMAL);
        worldCreator.generateStructures(true);

        World newWorld = Bukkit.createWorld(worldCreator);
        if (newWorld != null) {
            player.sendMessage(ChatColor.BLUE + "World 'world' created successfully.");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to create the world.");
        }
    }

    public static void teleportToWorld(Player player) {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            player.teleport(new Location(world, 0, world.getHighestBlockYAt(0, 0) + 5, 0));
            player.sendMessage(ChatColor.YELLOW + "Teleported to world!");
        } else {
            player.sendMessage(ChatColor.RED + "World 'world' does not exist!");
        }
    }

    public void deleteWorld(Player player) {
        World lobby = Bukkit.getWorld("lobby");
        if (lobby == null) {
            player.sendMessage(ChatColor.RED + "The lobby world does not exist!");
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Location spawnLocation = new Location(lobby, 0, lobby.getHighestBlockYAt(0, 0), 0);
            onlinePlayer.teleport(spawnLocation);
            onlinePlayer.sendMessage(ChatColor.YELLOW + "Teleported to lobby.");
        }

        World world = Bukkit.getWorld("world");
        if (world != null) {
            Bukkit.unloadWorld(world, false);  // false para no guardar los datos del mundo
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), "world");
        if (deleteWorldFiles(worldFolder)) {
            player.sendMessage(ChatColor.RED + "World 'world' deleted successfully.");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to delete world 'world'.");
        }
    }

    private static boolean deleteWorldFiles(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorldFiles(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return path.delete();
    }

    // Método para crear el mundo de Nether
    public static void createNetherWorld(Player player) {
        WorldCreator netherCreator = new WorldCreator("world_nether");
        netherCreator.environment(World.Environment.NETHER);
        netherCreator.type(WorldType.NORMAL);  // Tipo de Nether por defecto
        netherCreator.generateStructures(true);

        World netherWorld = Bukkit.createWorld(netherCreator);
        if (netherWorld != null) {
            player.sendMessage(ChatColor.DARK_RED + "Nether world 'world_nether' created successfully.");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to create Nether world.");
        }
    }

    // Método para cargar los chunks del Nether
    public static void loadNetherChunks(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world_nether set " + NUHC.getInstance().getGameConfig().getBorder() + " " + NUHC.getInstance().getGameConfig().getBorder() + " 0 0");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world_nether fill 125");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world_nether fill confirm");
        player.sendMessage(ChatColor.DARK_RED + "Nether chunks are being loaded.");
    }

    // Método para teletransportarse a una ubicación aleatoria en el Nether
    public static void teleportToRandomNether(Player player) {
        World netherWorld = Bukkit.getWorld("world_nether");
        if (netherWorld == null) {
            player.sendMessage(ChatColor.RED + "Nether world 'world_nether' does not exist!");
            return;
        }

        Random random = new Random();
        int x = random.nextInt(10000) - 5000; // Coordenadas X aleatorias entre -5000 y 5000
        int z = random.nextInt(10000) - 5000; // Coordenadas Z aleatorias entre -5000 y 5000

        // Iniciar la búsqueda desde y = 120 para evitar el techo del Nether
        for (int y = 120; y > 10; y--) { // Bajamos desde el techo hasta un nivel más bajo
            Location location = new Location(netherWorld, x, y, z);
            Location below = location.clone().subtract(0, 1, 0);
            Location above = location.clone().add(0, 1, 0);

            if (location.getBlock().getType() == Material.AIR && above.getBlock().getType() == Material.AIR && below.getBlock().getType().isSolid()) {
                player.teleport(location.add(0, 1, 0)); // Teletransportarse al lugar adecuado
                player.sendMessage(ChatColor.RED + "Teleported to a random location in the Nether!");
                return;
            }
        }

        player.sendMessage(ChatColor.RED + "Failed to find a safe location in the Nether.");
    }
}
