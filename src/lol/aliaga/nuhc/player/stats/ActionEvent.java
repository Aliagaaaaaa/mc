package lol.aliaga.nuhc.player.stats;

import lombok.Getter;

import java.time.LocalDateTime;

public class ActionEvent {

    @Getter
    private final String actionType;  // Type of action (mining, damage, death, etc.)

    @Getter
    private final String detail;  // Specific detail (diamond, gold, player damage, etc.)

    @Getter
    private final int amount;  // Quantity involved (blocks mined, damage, etc.)

    @Getter
    private final LocalDateTime timestamp;  // Timestamp of when the action occurred

    public ActionEvent(String actionType, String detail, int amount) {
        this.actionType = actionType;
        this.detail = detail;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();  // Record the current time
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s (%d)", timestamp, actionType, detail, amount);
    }
}
