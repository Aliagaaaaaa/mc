package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayerState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveAllCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player playerSender = (Player) sender;
        if (!playerSender.hasPermission("nuhc.admin.giveall")) {
            playerSender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 2) {
            playerSender.sendMessage(ChatColor.RED + "Usage: /giveall <item> <quantity>");
            return true;
        }

        Material material = Material.matchMaterial(args[0]);
        if (material == null) {
            playerSender.sendMessage(ChatColor.RED + "Invalid item: " + args[0]);
            return true;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(args[1]);
            if (quantity < 1 || quantity > 64) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            playerSender.sendMessage(ChatColor.RED + "Quantity must be between 1 and 64.");
            return true;
        }

        ItemStack itemToGive = new ItemStack(material, quantity);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isPlayerActive(player)) {
                player.getInventory().addItem(itemToGive);
                player.sendMessage(ChatColor.GREEN + "You have been given " + quantity + " "
                        + material.toString().toLowerCase() + "(s) by "
                        + ChatColor.YELLOW + playerSender.getName() + "!");
            }
        }

        return true;
    }

    private boolean isPlayerActive(Player player) {
        return NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId()).getState() == UHCPlayerState.PLAYER;
    }
}
