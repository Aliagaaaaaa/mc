package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.menus.TopKillsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class KillTopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            TopKillsMenu.openTopKillsMenu(player);
            return true;
        } else {
            sender.sendMessage("Este comando solo puede ser usado por jugadores.");
            return false;
        }
    }
}
