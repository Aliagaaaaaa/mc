package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveAllCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player playerSender = (Player) sender;

        // Verificar si el jugador tiene permiso
        if (!playerSender.hasPermission("nuhc.admin.giveall")) {
            playerSender.sendMessage("You don't have permission to use this command!");
            return true;
        }

        // Verificar que se hayan proporcionado los argumentos (mínimo: item y cantidad)
        if (args.length < 2) {
            playerSender.sendMessage("Usage: /giveall <item> <quantity>");
            return true;
        }

        // Obtener el ítem y la cantidad de los argumentos
        Material material = Material.matchMaterial(args[0]); // El nombre del ítem es el primer argumento
        if (material == null) {
            playerSender.sendMessage("Invalid item: " + args[0]);
            return true;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(args[1]); // La cantidad es el segundo argumento
        } catch (NumberFormatException e) {
            playerSender.sendMessage("Invalid quantity: " + args[1]);
            return true;
        }

        // Limitar la cantidad mínima y máxima
        if (quantity < 1 || quantity > 64) {
            playerSender.sendMessage("Quantity must be between 1 and 64.");
            return true;
        }

        // Iterar sobre todos los jugadores en el servidor
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Verificar que el jugador no sea un espectador, host o cualquier otro rol que no deba recibir el ítem
            if (isPlayerActive(player)) {
                // Crear el ítem que se le dará a los jugadores con la cantidad especificada
                ItemStack itemToGive = new ItemStack(material, quantity);

                // Darle el ítem al jugador
                player.getInventory().addItem(itemToGive);

                // Enviar mensaje al jugador
                player.sendMessage("You have been given " + quantity + " " + material.toString().toLowerCase() + "(s) by " + playerSender.getName() + "!");
            }
        }

        return true;
    }

    private boolean isPlayerActive(Player player) {
        return NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId()).getState()== UHCPlayerState.PLAYER;
    }
}
