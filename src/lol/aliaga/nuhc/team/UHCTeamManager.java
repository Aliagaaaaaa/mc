package lol.aliaga.nuhc.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UHCTeamManager {

    private final Map<String, UHCTeam> teams;

    public UHCTeamManager() {
        this.teams = new HashMap<>();
    }

    public boolean createTeam(Player leader) {
        String defaultName = "team_" + leader.getName();
        if (teams.containsKey(defaultName)) {
            leader.sendMessage(ChatColor.RED + "A team with that name already exists.");
            return false;
        }
        UHCTeam newTeam = new UHCTeam(leader);
        teams.put(defaultName, newTeam);
        leader.sendMessage(ChatColor.GREEN + "You have created the team " + ChatColor.WHITE + defaultName + "!");
        return true;
    }

    public UHCTeam getTeamByMemberName(String playerName) {
        return teams.values().stream()
                .filter(team -> team.getMembers().stream()
                        .map(Bukkit::getPlayer)
                        .anyMatch(member -> member != null && member.getName().equalsIgnoreCase(playerName)))
                .findFirst()
                .orElse(null);
    }

    public UHCTeam getTeam(String teamName) {
        return teams.get(teamName);
    }

    public UHCTeam getPlayerTeam(Player player) {
        return teams.values().stream()
                .filter(team -> team.getMembers().contains(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    public int getAliveTeams() {
        return (int) teams.values().stream()
                .filter(UHCTeam::isAlive)
                .count();
    }

    public void deleteTeam(String teamName) {
        teams.remove(teamName);
    }

    public Map<String, UHCTeam> getTeams() {
        return teams;
    }

    public boolean acceptTeamInvitation(Player player, String teamName) {
        UHCTeam team = teams.get(teamName);
        if (team == null) {
            player.sendMessage(ChatColor.RED + "There is no team with the name " + ChatColor.WHITE + teamName + ".");
            return false;
        }
        if (!team.hasInvitation(player)) {
            player.sendMessage(ChatColor.RED + "You do not have an invitation to join this team.");
            return false;
        }
        team.acceptInvitation(player);
        player.sendMessage(ChatColor.GREEN + "You have joined the team " + ChatColor.WHITE + teamName + "!");
        return true;
    }
}
