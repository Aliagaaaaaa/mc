package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.menus.WorldMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        openWorldMenu(player);
        return true;
    }

    private void openWorldMenu(Player player) {
        player.sendMessage(ChatColor.GREEN + "Opening the world menu...");
        WorldMenu.openWorldMenu(player);
    }
}