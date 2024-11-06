package lol.aliaga.nuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("nuhc.admin.heal")) {
            sender.sendMessage("You don't have permission to use this command!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Usage: /heal <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        // Curar al jugador completamente
        target.setHealth(target.getMaxHealth());
        target.setFoodLevel(20); // También se le llena la barra de hambre
        target.sendMessage("You have been healed by " + sender.getName() + "!");
        sender.sendMessage("You healed " + target.getName() + ".");
        return true;
    }
}
