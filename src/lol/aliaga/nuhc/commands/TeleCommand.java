package lol.aliaga.nuhc.commands;

import lol.aliaga.nuhc.NUHC;
import lol.aliaga.nuhc.player.UHCPlayerState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede ser utilizado por jugadores.");
            return false;
        }

        Player player = (Player) sender;

        // Verificar si se proporcionó un argumento (nombre del jugador)
        if (args.length != 1) {
            player.sendMessage("Uso correcto: /tele <jugador>");
            return false;
        }

        if(NUHC.getInstance().getUhcPlayerManager().getPlayer(player.getUniqueId()).getState() != UHCPlayerState.PLAYER) {
            player.sendMessage("No puedes usar este comando si estas vivo.");
            return false;
        }

        // Obtener el jugador objetivo
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage("El jugador " + args[0] + " no está en línea.");
            return false;
        }

        // Teletransportar al jugador que ejecutó el comando hacia el objetivo
        player.teleport(target);
        player.sendMessage("Te has teletransportado a " + target.getName());
        return true;
    }
}
