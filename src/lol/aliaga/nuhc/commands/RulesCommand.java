package lol.aliaga.nuhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RulesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage(ChatColor.AQUA + "      UHC RULES");
        player.sendMessage(ChatColor.WHITE + " The use of any illegal client/mod, cave/chest finder or xray texture pack will result in a permanent ban.");
        player.sendMessage(ChatColor.WHITE + " Using F3+a, F5 under lava, mining to entities, or anything that gives an unfair advantage will result in a ban.");
        player.sendMessage(ChatColor.WHITE + " You may relog during the game; you have 10 minutes to get back in.");
        player.sendMessage(ChatColor.WHITE + " Hackusating in game chat or spec chat will lead to a mute.");
        player.sendMessage(ChatColor.WHITE + " No stripmining or pokeholes below y32. Mining to sounds and player tags at any level is allowed.");
        player.sendMessage(ChatColor.WHITE + " Blastmining and pokeholing are not allowed and will result in a 3-day ban.");
        player.sendMessage(ChatColor.WHITE + " You may dig down at the end of a cave/ravine below y32 without going up first.");
        player.sendMessage(ChatColor.WHITE + " Hackusating in chat will lead to punishment.");
        player.sendMessage(ChatColor.WHITE + " If you relog during a heal, you will not receive it.");
        player.sendMessage(ChatColor.WHITE + " Do not stalk/follow " + ChatColor.AQUA + "Famous/Twitch/YouTube ranks" + ChatColor.WHITE + " during grace period.");
        player.sendMessage(ChatColor.WHITE + " If you die by a hacker, you can request a respawn only if the player is punished.");
        player.sendMessage(ChatColor.WHITE + " Skybasing is forbidden in 100x100 and may lead to a 1-week ban.");
        player.sendMessage(ChatColor.WHITE + " Check if shears work using the configuration.");
        player.sendMessage(ChatColor.WHITE + " No respawns or heals for block glitches or lag issues.");
        player.sendMessage(ChatColor.WHITE + " Stealing is allowed but excessive stalking may lead to a 3-day ban.");
        player.sendMessage(ChatColor.WHITE + " iPvP during the noPvP period is bannable.");
        player.sendMessage(ChatColor.WHITE + " If you relog during the final heal, you will not receive it.");
        player.sendMessage(ChatColor.WHITE + " Rollercoastering is allowed only from y32 to bedrock each time.");
        player.sendMessage(ChatColor.WHITE + " Use " + ChatColor.RED + "/scenarios" + ChatColor.WHITE + " to see scenarios and explanations.");
        player.sendMessage(ChatColor.WHITE + " World border will teleport you inside if you're outside the new border.");
        player.sendMessage(ChatColor.WHITE + " In the nether, you’ll be teleported to the world at 500x50. Nether radius is 500x500.");
        player.sendMessage(ChatColor.WHITE + " Ask questions via " + ChatColor.RED + "/helpop " + ChatColor.WHITE + "without spamming.");
        player.sendMessage(ChatColor.WHITE + " Use " + ChatColor.RED + "/config " + ChatColor.WHITE + "to check enabled features.");
        player.sendMessage(ChatColor.WHITE + " Type " + ChatColor.RED + "/scenarios " + ChatColor.WHITE + "for game scenarios.");
        player.sendMessage(ChatColor.WHITE + " Lying to the host for advantage results in a 1-day ban.");
        player.sendMessage(ChatColor.WHITE + " For unanswered questions, use chat to ask.");
        player.sendMessage(ChatColor.WHITE + " Have fun and thank your Host!");

        return true;
    }
}