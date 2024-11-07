package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.team.UHCTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;
        if (NUHC.getInstance().getGameConfig().getTeams() == 1) {
            player.sendMessage(ChatColor.RED + "Teams are not enabled!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /team <create|invite|accept|kick|leave|list>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "create":
                handleCreateTeam(player);
                break;
            case "invite":
                handleInvitePlayer(player, args);
                break;
            case "accept":
                handleAcceptInvite(player, args);
                break;
            case "kick":
                handleKickPlayer(player, args);
                break;
            case "leave":
                handleLeaveTeam(player);
                break;
            case "list":
                handleListTeamMembers(player);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown command.");
        }
        return true;
    }

    private void handleCreateTeam(Player player) {
        if (NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player) != null) {
            player.sendMessage(ChatColor.RED + "You are already in a team.");
            return;
        }
        if (NUHC.getInstance().getUhcTeamManager().createTeam(player)) {
            player.sendMessage(ChatColor.GREEN + "You have created a team.");
        }
    }

    private void handleInvitePlayer(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "You must specify a player to invite.");
            return;
        }
        String inviteeName = args[1];
        Player invitee = Bukkit.getPlayer(inviteeName);
        if (invitee == null) {
            player.sendMessage(ChatColor.RED + "The player is not online.");
            return;
        }
        UHCTeam playerTeam = NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player);
        if (playerTeam == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team.");
            return;
        }
        playerTeam.invitePlayer(invitee);
        player.sendMessage(ChatColor.GREEN + "You have invited " + inviteeName + " to your team.");
    }

    private void handleAcceptInvite(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "You must specify a team member to join.");
            return;
        }
        String memberName = args[1];
        if (NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player) != null) {
            player.sendMessage(ChatColor.RED + "You are already in a team.");
            return;
        }
        UHCTeam team = NUHC.getInstance().getUhcTeamManager().getTeamByMemberName(memberName);
        if (team == null) {
            player.sendMessage(ChatColor.RED + "No team found with a member named " + memberName + ".");
            return;
        }
        if (!team.hasInvitation(player)) {
            player.sendMessage(ChatColor.RED + "You do not have an invitation to join this team.");
            return;
        }
        team.acceptInvitation(player);
    }

    private void handleKickPlayer(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "You must specify a player to kick.");
            return;
        }
        String kickPlayerName = args[1];
        Player kickPlayer = Bukkit.getPlayer(kickPlayerName);
        if (kickPlayer == null) {
            player.sendMessage(ChatColor.RED + "The player is not online.");
            return;
        }
        UHCTeam playerTeam = NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player);
        UHCTeam kickPlayerTeam = NUHC.getInstance().getUhcTeamManager().getPlayerTeam(kickPlayer);
        if (playerTeam == null || kickPlayerTeam == null || !playerTeam.getName().equals(kickPlayerTeam.getName())) {
            player.sendMessage(ChatColor.RED + "You cannot kick this player because they are not in your team.");
            return;
        }
        if (!playerTeam.isLeader(player)) {
            player.sendMessage(ChatColor.RED + "Only the team leader can kick players.");
            return;
        }
        playerTeam.removeMember(kickPlayer);
        playerTeam.broadcast(ChatColor.YELLOW + kickPlayerName + " has been kicked from the team.");
    }

    private void handleLeaveTeam(Player player) {
        UHCTeam playerTeam = NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player);
        if (playerTeam == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team.");
            return;
        }
        playerTeam.removeMember(player);
        player.sendMessage(ChatColor.YELLOW + "You have left the team " + playerTeam.getName() + ".");
    }

    private void handleListTeamMembers(Player player) {
        UHCTeam playerTeam = NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player);
        if (playerTeam == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team.");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Team members of " + playerTeam.getName() + ":");
        for (UUID memberUUID : playerTeam.getMembers()) {
            Player member = Bukkit.getPlayer(memberUUID);
            if (member != null) {
                player.sendMessage(ChatColor.YELLOW + " - " + member.getName());
            }
        }
    }
}