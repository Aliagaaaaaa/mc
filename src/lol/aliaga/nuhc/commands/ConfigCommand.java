package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.menus.GameConfigMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            GameConfigMenu configMenu = new GameConfigMenu();
            configMenu.openMenu(player);
            return true;
        }
        sender.sendMessage("Only players can use this command.");
        return false;
    }
}