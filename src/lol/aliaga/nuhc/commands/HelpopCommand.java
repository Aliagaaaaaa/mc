package lol.aliaga.nuhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /helpop <message>");
            return true;
        }

        String message = String.join(" ", args);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String helpopMessage = ChatColor.GOLD + "[HelpOp] " + ChatColor.YELLOW + player.getName() + ": " + ChatColor.WHITE + message;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("uhc.helpop.view")) {
                    onlinePlayer.sendMessage(helpopMessage);
                }
            }

            player.sendMessage(ChatColor.GREEN + "Your message has been sent to the admins.");
        } else {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
        }

        return true;
    }
}
