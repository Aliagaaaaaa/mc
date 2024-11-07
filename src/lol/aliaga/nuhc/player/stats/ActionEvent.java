package lol.aliaga.nuhc.player.stats;

import java.time.LocalDateTime;

public class ActionEvent {

    private final String actionType;
    private final String detail;
    private final int amount;
    private final LocalDateTime timestamp;

    public ActionEvent(String actionType, String detail, int amount) {
        this.actionType = actionType;
        this.detail = detail;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s (%d)", timestamp, actionType, detail, amount);
    }
}
