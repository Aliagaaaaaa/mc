package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadChunksCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if(player.hasPermission("uhc.chunks")){
                Bukkit.getServer().dispatchCommand(commandSender, "wb shape square");
                Bukkit.getServer().dispatchCommand(commandSender, "wb world set " + NUHC.getInstance().getGameConfig().getBorder() + " " + NUHC.getInstance().getGameConfig().getBorder() + " 0 0");
                Bukkit.getServer().dispatchCommand(commandSender, "wb world fill 125");
                Bukkit.getServer().dispatchCommand(commandSender, "wb world fill confirm");
            }
        }
        return false;
    }
}
