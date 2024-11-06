package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameConfig;
import lol.aliaga.nuhc.scenarios.Scenario;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UHCCodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.hasPermission("uhc.command")) {
                if (args.length == 0) {
                    sendUsage(player);
                    return true;
                }

                String code = String.join(" ", args).trim();
                processCode(player, code);

            } else {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Console cannot use this command!");
        }
        return true;
    }

    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.GREEN + "Usage:");
        player.sendMessage(ChatColor.GREEN + "/uhccode <codigo> - Define entire game configuration using a code");
    }

    private void processCode(Player player, String code) {
        GameConfig config = NUHC.getInstance().getGameConfig();
        String[] pairs = code.split(";");

        for (String pair : pairs) {
            if (pair.length() < 2) continue;

            String option, value;

            if (pair.startsWith("ST") || pair.startsWith("PO") || pair.startsWith("BS") || pair.startsWith("FR") || pair.startsWith("MP") || pair.startsWith("SC")) {
                option = pair.substring(0, 2);
                value = pair.substring(2).trim();
            } else {
                option = pair.substring(0, 1);
                value = pair.substring(1).trim();
            }

            applyConfig(player, config, option, value);
        }
    }

    private void applyConfig(Player player, GameConfig config, String option, String value) {
        try {
            switch (option.toUpperCase()) {
                case "T": // teams
                    config.setTeams(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Teams set to " + value);
                    break;
                case "B": // border
                    config.setBorder(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Border set to " + value);
                    break;
                case "P": // pvpTime
                    config.setPvpTime(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "PvP time set to " + value + " minutes");
                    break;
                case "F": // finalHealTime
                    config.setFinalHealTime(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Final heal time set to " + value + " minutes");
                    break;
                case "E": // enderpearl
                    config.setEnderpearl(value.equals("1"));
                    player.sendMessage(ChatColor.GREEN + "Enderpearl usage set to " + value);
                    break;
                case "S": // speed
                    config.setSpeed(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Speed set to " + value);
                    break;
                case "ST": // strength
                    config.setStrength(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Strength set to " + value);
                    break;
                case "PO": // poison
                    config.setPoison(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Poison set to " + value);
                    break;
                case "A": // appleRate
                    config.setAppleRate(Float.parseFloat(value));
                    player.sendMessage(ChatColor.GREEN + "Apple rate set to " + value);
                    break;
                case "N": // nether
                    config.setNether(value.equals("1"));
                    player.sendMessage(ChatColor.GREEN + "Nether usage set to " + value);
                    break;
                case "BS": // borderShrinking
                    config.setBorderShrinking(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Border shrinking time set to " + value + " minutes");
                    break;
                case "FR": // flintRate
                    config.setFlintRate(Float.parseFloat(value));
                    player.sendMessage(ChatColor.GREEN + "Flint rate set to " + value);
                    break;
                case "MP": // maxPlayers
                    config.setMaxPlayers(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Max players set to " + value);
                    break;
                case "SC": // scenarios
                    processScenarios(player, config, value);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Unknown option: " + option);
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid value for " + option);
        }
    }

    private void processScenarios(Player player, GameConfig config, String scenarioList) {
        String[] scenarios = scenarioList.split(",");
        List<Scenario> newScenarioList = new ArrayList<>();

        for (String scenarioAbbreviation : scenarios) {
            String scenarioName = getScenarioFromAbbreviation(scenarioAbbreviation);
            Scenario newScenario = NUHC.getInstance().getScenarioManager().getScenario(scenarioName);

            if (newScenario != null) {
                newScenarioList.add(newScenario);
                player.sendMessage(ChatColor.GREEN + "Scenario '" + scenarioName + "' added.");
            } else {
                player.sendMessage(ChatColor.RED + "Scenario '" + scenarioName + "' not found.");
            }
        }

        config.setScenarios(newScenarioList);
        player.sendMessage(ChatColor.GREEN + "Scenarios updated.");
    }

    private String getScenarioFromAbbreviation(String abbreviation) {
        switch (abbreviation.toLowerCase()) {
            case "bp": return "Backpack";
            case "bb": return "Barebones";
            case "bd": return "BloodDiamond";
            case "be": return "BloodEnchant";
            case "bw": return "BowLess";
            case "cl": return "CoalLess";
            case "cc": return "Cutclean";
            case "dl": return "DiamondLess";
            case "fl": return "FireLess";
            case "gl": return "GoldLess";
            case "hl": return "HorseLess";
            case "il": return "IronLess";
            case "rl": return "RodLess";
            case "tb": return "Timebomb";
            default: return null;
        }
    }
}
