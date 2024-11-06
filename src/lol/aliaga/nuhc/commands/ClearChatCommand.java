package lol.aliaga.nuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("nuhc.admin.clearchat")) {
            sender.sendMessage("You don't have permission to use this command!");
            return true;
        }

        for (int i = 0; i < 1000; i++) {
            Bukkit.broadcastMessage("");
        }

        Bukkit.broadcastMessage("Chat has been cleared by " + sender.getName() + ".");
        return true;
    }
}
