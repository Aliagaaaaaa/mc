package lol.aliaga.nuhc.team;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayer;
import lol.aliaga.nuhc.player.stats.StatType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UHCTeam {

    @Getter
    private final String name;
    @Getter
    private final UUID leader;
    @Getter
    private final List<UUID> members;
    private final List<UUID> invitations;
    @Setter
    @Getter
    private boolean alive;

    public UHCTeam(Player leader) {
        this.name = "team_" + leader.getName();
        this.leader = leader.getUniqueId();
        this.members = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.alive = true;
        this.members.add(leader.getUniqueId());
    }

    public void addMember(Player player) {
        if (!invitations.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You do not have an invitation to join this team.");
            return;
        }
        members.add(player.getUniqueId());
        invitations.remove(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You have joined the team " + ChatColor.WHITE + name + "!");
        broadcast(player.getName() + " has joined the team.");
    }

    public void removeMember(Player player) {
        members.remove(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "You have left the team " + ChatColor.WHITE + name + ".");
        broadcast(player.getName() + " has left the team.");
    }

    public boolean isLeader(Player player) {
        return player.getUniqueId().equals(leader);
    }

    public void invitePlayer(Player player) {
        if (invitations.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "This player already has an invitation.");
            return;
        }
        invitations.add(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "You have been invited to join team " + ChatColor.WHITE + name + ". Use /team accept to join.");
    }

    public boolean hasInvitation(Player player) {
        return invitations.contains(player.getUniqueId());
    }

    public void acceptInvitation(Player player) {
        if (hasInvitation(player)) {
            addMember(player);
        } else {
            player.sendMessage(ChatColor.RED + "You do not have an invitation from this team.");
        }
    }

    public void broadcast(String message) {
        for (UUID memberId : members) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
                member.sendMessage(ChatColor.GOLD + "[Team " + name + "] " + ChatColor.WHITE + message);
            }
        }
    }

    public void listMembers(Player requester) {
        requester.sendMessage(ChatColor.GREEN + "Members of team " + ChatColor.WHITE + name + ":");
        for (UUID memberId : members) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
                requester.sendMessage(ChatColor.YELLOW + " - " + member.getName());
            }
        }
    }

    public int getTeamKills() {
        return members.stream()
                .mapToInt(memberId -> NUHC.getInstance().getUhcPlayerManager().getPlayer(memberId).getStats().getStat(StatType.KILLS))
                .sum();
    }
}
