package lol.aliaga.nuhc.staff;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class UHCStaff {

    private final UUID uniqueId;
    private boolean miningNotifications;
    private boolean pvpNotifications;

    public UHCStaff(Player player) {
        this.uniqueId = player.getUniqueId();
        this.miningNotifications = true;  // Enabled by default
        this.pvpNotifications = true;    // Enabled by default
    }
}
