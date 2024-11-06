package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameConfig;
import lol.aliaga.nuhc.game.GameState;
import lol.aliaga.nuhc.player.UHCPlayer;
import lol.aliaga.nuhc.player.UHCPlayerState;
import lol.aliaga.nuhc.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("uhc.command")) {
                if (args.length == 0) {
                    sendUsageMessage(player);
                } else if (args[0].equalsIgnoreCase("setupfinish")) {
                    handleSetupFinish(player);
                } else if (args[0].equalsIgnoreCase("host")) {
                    handleHostCommand(player, args);
                } else if (args[0].equalsIgnoreCase("admin")) {
                    handleAdminCommand(player, args);
                } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec")) {
                    handleSpectatorCommand(player, args);
                } else if (args[0].equalsIgnoreCase("edit")) {
                    handleEditCommand(player, args);
                } else {
                    player.sendMessage(ChatColor.RED + "Unknown command or invalid syntax.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Console cannot use this command!");
        }
        return false;
    }

    private void sendUsageMessage(Player player) {
        player.sendMessage(ChatColor.GREEN + "Usage:");
        player.sendMessage(ChatColor.GREEN + "/uhc host <player> - Set a player as host");
        player.sendMessage(ChatColor.GREEN + "/uhc admin <player> - Set a player as admin");
        player.sendMessage(ChatColor.GREEN + "/uhc spectator <player> - Set a player as spectator");
        player.sendMessage(ChatColor.GREEN + "/uhc edit <option> <value> - Edit game configuration");
        player.sendMessage(ChatColor.GREEN + "/uhc setupfinish - Check for configuration inconsistencies and set the game to waiting start");
    }

    private void handleSetupFinish(Player player) {
        GameConfig config = NUHC.getInstance().getGameConfig();
        boolean hasErrors = false;

        if (!config.isNether() && (config.getStrength() > 0 || config.getPoison() > 0)) {
            player.sendMessage(ChatColor.RED + "Error: Nether is disabled, so Strength and Poison potions should be disabled.");
            hasErrors = true;
        }

        for (Scenario scenario : config.getScenarios()) {
            if (config.getTeams() == 1 && scenario.getName().equalsIgnoreCase("Backpack")) {
                player.sendMessage(ChatColor.RED + "Error: The 'Backpack' scenario cannot be enabled in solo mode (Teams = 1).");
                hasErrors = true;
            }
        }

        if (!hasErrors) {
            player.sendMessage(ChatColor.GREEN + "UHC setup is ready! Changing gamestate to WAITING_START");
            NUHC.getInstance().setGameState(GameState.WAITING_START);
        } else {
            player.sendMessage(ChatColor.RED + "There are configuration issues that need to be resolved.");
        }
    }

    private void handleHostCommand(Player player, String[] args) {
        if (args.length == 1) {
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
            if (uhcPlayer != null) {
                uhcPlayer.setState(UHCPlayerState.HOST);
                player.sendMessage(ChatColor.GREEN + "You are now host of the match");
            }
        } else if (args.length == 2) {
            setPlayerState(player, args[1], UHCPlayerState.HOST, "host");
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /uhc host <player>");
        }
    }

    private void handleAdminCommand(Player player, String[] args) {
        if (args.length == 1) {
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
            if (uhcPlayer != null) {
                uhcPlayer.setState(UHCPlayerState.HOST);
                player.sendMessage(ChatColor.GREEN + "You are now admin of the match");
            }
        } else if (args.length == 2) {
            setPlayerState(player, args[1], UHCPlayerState.HOST, "admin");
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /uhc admin <player>");
        }
    }

    private void handleSpectatorCommand(Player player, String[] args) {
        if (args.length == 1) {
            UHCPlayer uhcPlayer = NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId());
            if (uhcPlayer != null) {
                uhcPlayer.setState(UHCPlayerState.SPECTATOR);
                player.sendMessage(ChatColor.GREEN + "You are now spectator of the match");
            }
        } else if (args.length == 2) {
            setPlayerState(player, args[1], UHCPlayerState.SPECTATOR, "spectator");
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /uhc spectator <player>");
        }
    }

    private void setPlayerState(Player player, String targetName, UHCPlayerState state, String role) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
        if (offlinePlayer != null) {
            UHCPlayer target = NUHC.getInstance().getUhcPlayerManager().getPlayer(offlinePlayer.getUniqueId());
            if (target == null) {
                NUHC.getInstance().getUhcPlayerManager().createPlayer(offlinePlayer.getUniqueId());
            }

            target = NUHC.getInstance().getUhcPlayerManager().getPlayer(offlinePlayer.getUniqueId());
            target.setState(state);
            player.sendMessage(ChatColor.GREEN + offlinePlayer.getName() + " is now " + role + " of the match");
            if (offlinePlayer.isOnline()) {
                offlinePlayer.getPlayer().sendMessage(ChatColor.GREEN + "You are now " + role + " of the match");
            }
        } else {
            player.sendMessage(ChatColor.RED + "That player never joined the server!");
        }
    }

    private void handleEditCommand(Player player, String[] args) {
        GameConfig config = NUHC.getInstance().getGameConfig();

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /uhc edit <option> <value>");
            return;
        }

        String option = args[1];
        String value = args[2];

        try {
            switch (option.toLowerCase()) {
                case "teams":
                    config.setTeams(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Teams set to " + value);
                    break;
                case "border":
                    config.setBorder(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Border set to " + value);
                    break;
                case "pvptime":
                    config.setPvpTime(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "PvP time set to " + value + " minutes");
                    break;
                case "finalhealtime":
                    config.setFinalHealTime(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Final heal time set to " + value + " minutes");
                    break;
                case "enderpearl":
                    config.setEnderpearl(Boolean.parseBoolean(value));
                    player.sendMessage(ChatColor.GREEN + "Enderpearl usage set to " + value);
                    break;
                case "speed":
                    config.setSpeed(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Speed set to " + value);
                    break;
                case "strength":
                    config.setStrength(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Strength set to " + value);
                    break;
                case "poison":
                    config.setPoison(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Poison set to " + value);
                    break;
                case "applerate":
                    config.setAppleRate(Float.parseFloat(value));
                    player.sendMessage(ChatColor.GREEN + "Apple rate set to " + value);
                    break;
                case "nether":
                    config.setNether(Boolean.parseBoolean(value));
                    player.sendMessage(ChatColor.GREEN + "Nether usage set to " + value);
                    break;
                case "bordershrinking":
                    config.setBorderShrinking(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Border shrinking time set to " + value + " minutes");
                    break;
                case "flintrate":
                    config.setFlintRate(Float.parseFloat(value));
                    player.sendMessage(ChatColor.GREEN + "Flint rate set to " + value);
                    break;
                case "maxplayers":
                    config.setMaxPlayers(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Max players set to " + value);
                    break;
                case "practice":
                    config.setPractice(Boolean.parseBoolean(value));
                    player.sendMessage(ChatColor.GREEN + "Practice mode set to " + value);
                    break;
                case "netherborder":
                    config.setNetherBorder(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Nether border set to " + value);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Unknown option: " + option);
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid value for " + option);
        }
    }
}
