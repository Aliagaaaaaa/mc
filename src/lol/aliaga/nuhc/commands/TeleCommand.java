package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Correct usage: /tele <player>");
            return false;
        }

        if (NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId()).getState() != UHCPlayerState.PLAYER) {
            player.sendMessage(ChatColor.RED + "You cannot use this command while you are alive.");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Player " + args[0] + " is not online.");
            return false;
        }

        player.teleport(target);
        player.sendMessage(ChatColor.GREEN + "You have been teleported to " + ChatColor.WHITE + target.getName() + ChatColor.GREEN + ".");
        return true;
    }
}