package lol.aliaga.nuhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RulesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage(ChatColor.AQUA + "      UHC RULES");
            player.sendMessage(ChatColor.WHITE + " The use of any illegal client/mod, cave/chest finder or x-ray texture pack will result in a permanent ban.");
            player.sendMessage(ChatColor.WHITE + " Using F3+a, F5 under lava, mining to entities or doing anything that gives you an unfair advantage will result in a permanent ban.");
            player.sendMessage(ChatColor.WHITE + " You may relog during the game. You have 10 minutes to get back in.");
            player.sendMessage(ChatColor.WHITE + " Hackusating in game chat or in spec chat will lead to a mute.");
            player.sendMessage(ChatColor.WHITE + " No stripmining or pokeholes below y32. You may mine to sounds and player tags at any level.");
            player.sendMessage(ChatColor.WHITE + " Blastmining and pokeholing are not allowed. They're equal to a 3 days ban.");
            player.sendMessage(ChatColor.WHITE + " You may mine down without going up first if you’re at the end of a cave or ravine, even if it’s below y32.");
            player.sendMessage(ChatColor.WHITE + " Hackusating in game chat or in spec chat will lead to a punishment.");
            player.sendMessage(ChatColor.WHITE + " If you relog during a heal, you will not receive it.");
            player.sendMessage(ChatColor.WHITE + " Do not stalk/follow " + ChatColor.AQUA + "Famous/Twitch/YouTube ranks" + ChatColor.WHITE + " during grace period. We can TP you away if you do.");
            player.sendMessage(ChatColor.WHITE + " If you die by a hacker, you can request a respawn only if the player has been punished.");
            player.sendMessage(ChatColor.WHITE + " Skybasing is forbidden in 100x100. It can result in a 1 week ban.");
            player.sendMessage(ChatColor.WHITE + " To check if shears work, use the configuration.");
            player.sendMessage(ChatColor.WHITE + " No respawns or heals due to block glitches or lag.");
            player.sendMessage(ChatColor.WHITE + " Stealing is allowed, like stalking, but not excessively. It can result in a 3 days ban.");
            player.sendMessage(ChatColor.WHITE + " iPvP during the no-PvP period is not allowed and is bannable.");
            player.sendMessage(ChatColor.WHITE + " If you relog during the final heal, you will not receive it.");
            player.sendMessage(ChatColor.WHITE + " You may relog during the game but you only have 10 minutes to get back in.");
            player.sendMessage(ChatColor.WHITE + " Rollercoastering (staircasing up and down) is only allowed if you go from y32 to bedrock each time.");
            player.sendMessage(ChatColor.WHITE + " Use " + ChatColor.RED + "/scenarios" + ChatColor.WHITE + " to see scenarios and their explanation.");
            player.sendMessage(ChatColor.WHITE + " The world border will teleport you inside the new border if you are outside it.");
            player.sendMessage(ChatColor.WHITE + " If you're in the nether, you will be teleported back to the world at 500x50.");
            player.sendMessage(ChatColor.WHITE + " The nether only has a radius of 500x500.");
            player.sendMessage(ChatColor.WHITE + " If you have some questions, ask it via " + ChatColor.RED + "/helpop " + ChatColor.WHITE + "and do not spam it.");
            player.sendMessage(ChatColor.WHITE + " Type " + ChatColor.RED + "/config " + ChatColor.WHITE + "to see what is enabled and what is not.");
            player.sendMessage(ChatColor.WHITE + " Type " + ChatColor.RED + "/scenarios " + ChatColor.WHITE + "to learn about the game scenarios.");
            player.sendMessage(ChatColor.WHITE + " Lying to the host in-order to get an advantage will result in a 1 day ban.");
            player.sendMessage(ChatColor.WHITE + " If you have any questions not answered by " + ChatColor.RED + "/config" + ChatColor.WHITE + ", " + ChatColor.RED + "/scenarios " + ChatColor.WHITE + "or rules, ask in chat.");
            player.sendMessage(ChatColor.WHITE + " Have fun and don’t forget to thank your Host!");

        } else {
            sender.sendMessage("Only players can use this command.");
        }

        return true;
    }
}
