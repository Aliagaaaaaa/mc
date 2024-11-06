package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.team.UHCTeam;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "You must write a message to send to your team.");
            return true;
        }

        UHCTeam playerTeam = NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player);
        if (playerTeam == null) {
            player.sendMessage(ChatColor.RED + "You are not in any team.");
            return true;
        }

        String message = String.join(" ", args);

        sendTeamMessage(player, playerTeam, message);
        return true;
    }

    private void sendTeamMessage(Player player, UHCTeam team, String message) {
        team.broadcast(ChatColor.AQUA + "[TeamChat] " + ChatColor.WHITE + player.getName() + ": " + message);
        player.sendMessage(ChatColor.AQUA + "[TeamChat] " + ChatColor.WHITE + "Your message was sent to the team.");
    }
}
