package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.menus.WorldMenu;
import org.bukkit.command.CommandExecutor;

public class WorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof org.bukkit.entity.Player) {
            WorldMenu.openWorldMenu((org.bukkit.entity.Player) sender);
            return true;
        }
        return false;
    }
}
