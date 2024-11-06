package lol.aliaga.nuhc.player;

import lol.aliaga.nuhc.player.stats.ActionEvent;
import lol.aliaga.nuhc.player.stats.UHCStats;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UHCPlayer {

    @Getter
    private final UUID uniqueId;

    @Getter
    private final UHCStats stats;  // Player stats

    @Getter
    @Setter
    private UHCPlayerState state;

    public UHCPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.state = UHCPlayerState.PLAYER;
        this.stats = new UHCStats();
    }

    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) {
            player.sendMessage(message);
        }
    }

    public int diamondsMined() {
        return (int) stats.getActionEvents().stream()
                .filter(event -> "Minería".equalsIgnoreCase(event.getActionType()) && event.getDetail().contains("diamante"))
                .count();
    }
}
