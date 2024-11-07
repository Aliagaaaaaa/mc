package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.menus.ScenariosMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScenariosCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        openScenariosMenu(player);
        return true;
    }

    private void openScenariosMenu(Player player) {
        ScenariosMenu menu = new ScenariosMenu();
        player.sendMessage(ChatColor.GREEN + "Opening scenarios menu...");
        menu.openMenu(player);
    }
}