package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.team.UHCTeam;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCoordsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;
        UHCTeam playerTeam = NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player);

        if (playerTeam == null) {
            player.sendMessage(ChatColor.RED + "You are not part of any team.");
            return true;
        }

        sendTeamCoordinates(player, playerTeam);
        return true;
    }

    private void sendTeamCoordinates(Player player, UHCTeam team) {
        Location location = player.getLocation();
        String coordinatesMessage = ChatColor.AQUA + "[TeamCoords] " + ChatColor.WHITE + player.getName() +
                " is at X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ();

        team.broadcast(coordinatesMessage);
        player.sendMessage(ChatColor.GREEN + "Your coordinates were sent to your team.");
    }
}