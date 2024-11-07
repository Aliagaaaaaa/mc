package lol.aliaga.nuhc.player;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UHCPlayerManager {
    private final Map<UUID, UHCPlayer> players;

    public UHCPlayerManager() {
        this.players = new HashMap<>();
    }

    public UHCPlayer getPlayer(UUID uniqueId) {
        return players.get(uniqueId);
    }

    public void createPlayer(UUID uniqueId) {
        if(players.containsKey(uniqueId)) {
            return;
        }
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

