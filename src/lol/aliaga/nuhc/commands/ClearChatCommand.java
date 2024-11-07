package lol.aliaga.nuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("nuhc.admin.clearchat")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcastMessage(""); // Disminuido a 100 líneas para evitar exceso de spam en el chat
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "Chat has been cleared by " + ChatColor.YELLOW + sender.getName() + ".");
        return true;
    }
}

