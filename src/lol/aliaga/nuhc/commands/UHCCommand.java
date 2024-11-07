package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.game.GameConfig;
import lol.aliaga.nuhc.game.GameState;
import lol.aliaga.nuhc.player.UHCPlayer;
import lol.aliaga.nuhc.player.UHCPlayerState;
import lol.aliaga.nuhc.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UHCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Console cannot use this command!");
            return true;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("uhc.command")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            sendUsageMessage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "setupfinish":
                handleSetupFinish(player);
                break;
            case "host":
                handleHostCommand(player, args);
                break;
            case "admin":
                handleAdminCommand(player, args);
                break;
            case "spectator":
            case "spec":
                handleSpectatorCommand(player, args);
                break;
            case "edit":
                handleEditCommand(player, args);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown command or invalid syntax.");
        }

        return true;
    }

    private void sendUsageMessage(Player player) {
        player.sendMessage(ChatColor.GREEN + "Usage:");
        player.sendMessage(ChatColor.GREEN + "/uhc host <player> " + ChatColor.GRAY + "Set a player as host");
        player.sendMessage(ChatColor.GREEN + "/uhc admin <player> " + ChatColor.GRAY + "Set a player as admin");
        player.sendMessage(ChatColor.GREEN + "/uhc spectator <player> " + ChatColor.GRAY + "Set a player as spectator");
        player.sendMessage(ChatColor.GREEN + "/uhc edit <option> <value> " + ChatColor.GRAY + "Edit game configuration");
        player.sendMessage(ChatColor.GREEN + "/uhc setupfinish " + ChatColor.GRAY + "Check configuration and set game to waiting start");
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

        if (hasErrors) {
            player.sendMessage(ChatColor.RED + "There are configuration issues that need to be resolved.");
        } else {
            player.sendMessage(ChatColor.GREEN + "UHC setup is ready! Changing game state to " + ChatColor.WHITE + "WAITING_START");
            NUHC.getInstance().setGameState(GameState.WAITING_START);
        }
    }

    private void handleHostCommand(Player player, String[] args) {
        if (args.length == 1) {
            setPlayerRole(player, player.getName(), UHCPlayerState.HOST, "host");
        } else if (args.length == 2) {
            setPlayerRole(player, args[1], UHCPlayerState.HOST, "host");
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /uhc host <player>");
        }
    }

    private void handleAdminCommand(Player player, String[] args) {
        if (args.length == 1) {
            setPlayerRole(player, player.getName(), UHCPlayerState.ADMIN, "admin");
        } else if (args.length == 2) {
            setPlayerRole(player, args[1], UHCPlayerState.ADMIN, "admin");
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /uhc admin <player>");
        }
    }

    private void handleSpectatorCommand(Player player, String[] args) {
        if (args.length == 1) {
            setPlayerRole(player, player.getName(), UHCPlayerState.SPECTATOR, "spectator");
        } else if (args.length == 2) {
            setPlayerRole(player, args[1], UHCPlayerState.SPECTATOR, "spectator");
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /uhc spectator <player>");
        }
    }

    private void setPlayerRole(Player player, String targetName, UHCPlayerState state, String role) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetName);
        UHCPlayer target = NUHC.getInstance().getUhcPlayerManager().getPlayer(offlinePlayer.getUniqueId());

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player never joined the server!");
            return;
        }

        target.setState(state);
        player.sendMessage(ChatColor.GREEN + offlinePlayer.getName() + " is now " + ChatColor.WHITE + role + ChatColor.GREEN + " of the match");

        if (offlinePlayer.isOnline()) {
            Player onlineTarget = offlinePlayer.getPlayer();
            setSpectatorSettings(onlineTarget, state, role);
        }
    }

    private void setSpectatorSettings(Player player, UHCPlayerState state, String role) {
        if (state == UHCPlayerState.SPECTATOR || state == UHCPlayerState.SPECTATOR_DEATH) {
            player.setGameMode(GameMode.SPECTATOR);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(ChatColor.YELLOW + "You are in spectator mode as " + role + ".");
        } else if (state == UHCPlayerState.HOST || state == UHCPlayerState.ADMIN) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(ChatColor.YELLOW + "You have been set as " + role + " with fly enabled.");
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(ChatColor.YELLOW + "You are in player mode.");
        }
    }

    private void handleEditCommand(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /uhc edit <option> <value>");
            return;
        }

        String option = args[1];
        String value = args[2];
        GameConfig config = NUHC.getInstance().getGameConfig();

        try {
            switch (option.toLowerCase()) {
                case "teams":
                    config.setTeams(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Teams set to " + ChatColor.WHITE + value);
                    break;
                case "border":
                    config.setBorder(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Border set to " + ChatColor.WHITE + value);
                    break;
                case "pvptime":
                    config.setPvpTime(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "PvP time set to " + ChatColor.WHITE + value + ChatColor.GREEN + " minutes");
                    break;
                case "finalhealtime":
                    config.setFinalHealTime(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Final heal time set to " + ChatColor.WHITE + value + ChatColor.GREEN + " minutes");
                    break;
                case "enderpearl":
                    config.setEnderpearl(Boolean.parseBoolean(value));
                    player.sendMessage(ChatColor.GREEN + "Enderpearl usage set to " + ChatColor.WHITE + value);
                    break;
                case "speed":
                    config.setSpeed(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Speed set to " + ChatColor.WHITE + value);
                    break;
                case "strength":
                    config.setStrength(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Strength set to " + ChatColor.WHITE + value);
                    break;
                case "poison":
                    config.setPoison(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Poison set to " + ChatColor.WHITE + value);
                    break;
                case "applerate":
                    config.setAppleRate(Float.parseFloat(value));
                    player.sendMessage(ChatColor.GREEN + "Apple rate set to " + ChatColor.WHITE + value);
                    break;
                case "nether":
                    config.setNether(Boolean.parseBoolean(value));
                    player.sendMessage(ChatColor.GREEN + "Nether usage set to " + ChatColor.WHITE + value);
                    break;
                case "bordershrinking":
                    config.setBorderShrinking(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Border shrinking time set to " + ChatColor.WHITE + value + ChatColor.GREEN + " minutes");
                    break;
                case "flintrate":
                    config.setFlintRate(Float.parseFloat(value));
                    player.sendMessage(ChatColor.GREEN + "Flint rate set to " + ChatColor.WHITE + value);
                    break;
                case "maxplayers":
                    config.setMaxPlayers(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Max players set to " + ChatColor.WHITE + value);
                    break;
                case "practice":
                    config.setPractice(Boolean.parseBoolean(value));
                    player.sendMessage(ChatColor.GREEN + "Practice mode set to " + ChatColor.WHITE + value);
                    break;
                case "netherborder":
                    config.setNetherBorder(Integer.parseInt(value));
                    player.sendMessage(ChatColor.GREEN + "Nether border set to " + ChatColor.WHITE + value);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Unknown option: " + option);
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid value for " + option);
        }
    }
}
