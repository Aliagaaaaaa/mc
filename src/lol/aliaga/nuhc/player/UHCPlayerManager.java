package lol.aliaga.nuhc.player;

import lol.aliaga.nuhc.NUHC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class UHCPlayerManager {

    private final Map<UUID, UHCPlayer> players;

    public UHCPlayerManager() {
        this.players = new HashMap<>();
    }

    public UHCPlayer getPlayer(UUID uniqueId) {
        return players.get(uniqueId);
    }

    public void createPlayer(UUID uniqueId) {
        players.put(uniqueId, new UHCPlayer(uniqueId));
    }

    public int getPlayerCount() {
        return (int) players.values().stream()
                .filter(player -> player.getState() == UHCPlayerState.PLAYER)
                .count();
    }

    public int getSpectatorCount() {
        return (int) Bukkit.getOnlinePlayers().stream()
                .filter(player -> getPlayer(player.getUniqueId()).getState() != UHCPlayerState.PLAYER)
                .count();
    }

    public List<UHCPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }
}
