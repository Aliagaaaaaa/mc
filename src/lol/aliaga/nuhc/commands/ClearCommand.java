package lol.aliaga.nuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("nuhc.admin.clear")) {
            sender.sendMessage("You don't have permission to use this command!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Usage: /clear <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        // Limpiar el inventario del jugador
        target.getInventory().clear();
        target.sendMessage("Your inventory has been cleared by " + sender.getName() + "!");
        sender.sendMessage("You cleared the inventory of " + target.getName() + ".");
        return true;
    }
}
