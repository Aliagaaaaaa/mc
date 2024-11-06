package lol.aliaga.nuhc.board;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UHCBoard implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return ChatColor.AQUA + "" + ChatColor.BOLD +"Budas UHC";
    }

    @Override
    public List<String> getLines(Player player) {
        final List<String> toReturn = new ArrayList<>();
        switch (NUHC.getInstance().getGameState()){
            case SETUP:
                toReturn.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------");
                toReturn.add(ChatColor.GREEN + "Editing game...");
                toReturn.add("");
                toReturn.add(ChatColor.GREEN + "Mode: " + ChatColor.WHITE + (NUHC.getInstance().getGameConfig().getTeams() == 1 ? "FFA" : "To"+NUHC.getInstance().getGameConfig().getTeams()));
                toReturn.add(ChatColor.GREEN + "Border: " + ChatColor.WHITE + (NUHC.getInstance().getGameConfig().getBorder()));
                toReturn.add(ChatColor.GREEN + "PvP Time: " + ChatColor.WHITE + (NUHC.getInstance().getGameConfig().getPvpTime() + "min"));
                toReturn.add(ChatColor.GREEN + "Final heal: " + ChatColor.WHITE + (NUHC.getInstance().getGameConfig().getFinalHealTime() + "min"));
                toReturn.add("");
                toReturn.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------");
                toReturn.add(ChatColor.AQUA + "www.budas.club");



                break;
            case WAITING_START:
            case STARTING:
                toReturn.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------");
                toReturn.add(ChatColor.GREEN + "Team: " + ChatColor.WHITE + (NUHC.getInstance().getGameConfig().getTeams() == 1 ? "FFA" : "To"+NUHC.getInstance().getGameConfig().getTeams()));
                toReturn.add("");
                toReturn.add(ChatColor.GREEN + "Gamemode: ");
                if(NUHC.getInstance().getGameConfig().getScenarios().isEmpty()){
                    toReturn.add(ChatColor.WHITE + "None");
                }

                for(Scenario scenario : NUHC.getInstance().getGameConfig().getScenarios()){
                    toReturn.add(ChatColor.WHITE + "- " + scenario.getName());
                }
                toReturn.add("");
                toReturn.add(ChatColor.GREEN + "Players: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size());
                toReturn.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------");
                toReturn.add(ChatColor.AQUA + "www.budas.club");

                break;

            case IN_GAME:
                toReturn.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-------------------");
                toReturn.add(ChatColor.GREEN + "Game Time: " + ChatColor.WHITE + parseTime(NUHC.seconds));
                toReturn.add("");
                toReturn.add(ChatColor.GREEN + "Your Kills: " + ChatColor.WHITE + NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId()).getStats().getKills());
                if(NUHC.getInstance().getGameConfig().getTeams() != 1){
                    toReturn.add(ChatColor.GREEN + "Team Kills: " + ChatColor.WHITE + NUHC.getInstance().getUhcTeamManager().getPlayerTeam(player).getTeamKills());
                }
                toReturn.add("");
                if(NUHC.getInstance().getGameConfig().getTeams() != 1){
                    toReturn.add(ChatColor.GREEN + "Teams Left: " + ChatColor.WHITE + NUHC.getInstance().getUhcTeamManager().getAliveTeams());
                }
                toReturn.add(ChatColor.GREEN + "Players Left: " + ChatColor.WHITE + NUHC.getInstance().getUhcPlayerManager().getPlayerCount());
                toReturn.add(ChatColor.GREEN + "Spectators: " + ChatColor.WHITE + NUHC.getInstance().getUhcPlayerManager().getSpectatorCount());
                toReturn.add("");
                toReturn.add(ChatColor.GOLD + "Current Border: " + ChatColor.WHITE + NUHC.getInstance().getCurrentBorder());
                toReturn.add(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-------------------");
                toReturn.add(ChatColor.AQUA + "www.budas.club");


                break;
            case ENDED:

                break;
        }

        return toReturn;
    }

    public static String parseTime(long seconds) {  // Cambiado el parámetro a 'seconds'

        if (seconds < 60) {
            return String.format("00:00:%02d", seconds); // Añadido formato hh:mm:ss
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            return String.format("00:%02d:%02d", minutes, remainingSeconds); // Añadido formato hh:mm:ss
        } else {
            long hours = seconds / 3600;
            long remainingMinutes = (seconds % 3600) / 60;
            long remainingSeconds = seconds % 60;
            return String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds);
        }
    }


}
