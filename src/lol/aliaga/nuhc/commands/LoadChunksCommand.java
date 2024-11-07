package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadChunksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("uhc.chunks")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        Player player = (Player) sender;
        Bukkit.getServer().dispatchCommand(player, "wb shape square");
        Bukkit.getServer().dispatchCommand(player, "wb world set "
                + NUHC.getInstance().getGameConfig().getBorder() + " "
                + NUHC.getInstance().getGameConfig().getBorder() + " 0 0");
        Bukkit.getServer().dispatchCommand(player, "wb world fill 125");
        Bukkit.getServer().dispatchCommand(player, "wb world fill confirm");

        player.sendMessage(ChatColor.GREEN + "Chunks are being loaded...");
        return true;
    }
}