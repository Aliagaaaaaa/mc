package lol.aliaga.nuhc.player.stats;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UHCStats {

    @Getter
    private int kills;

    @Getter
    private boolean isDead;  // Check if the player has already died

    @Getter
    private int expGained;  // Total experience gained

    @Getter
    private int goldenApplesConsumed;  // Total golden apples consumed

    @Getter
    private int goldenHeadsConsumed;  // Total Golden Heads consumed

    @Getter
    private int arrowsShot;  // Arrows shot

    @Getter
    private int arrowsHit;  // Arrows hit

    @Getter
    private int timesRiddenHorse;  // Number of times the player has ridden a horse

    @Getter
    private List<ActionEvent> actionEvents;  // List of all actions with timestamps

    public UHCStats() {
        this.kills = 0;
        this.isDead = false;
        this.expGained = 0;
        this.goldenApplesConsumed = 0;
        this.goldenHeadsConsumed = 0;
        this.arrowsShot = 0;
        this.arrowsHit = 0;
        this.timesRiddenHorse = 0;
        this.actionEvents = new ArrayList<>();
    }

    public void addKill() {
        this.kills++;
        addActionEvent("Kill", "Player killed", 1);
    }

    public void addDeath() {
        if (!isDead) {
            this.isDead = true;
            addActionEvent("Death", "You died", 1);
        }
    }

    // Record damage dealt
    public void addDamageDealt(String targetName, double amount) {
        addActionEvent("Damage", "Damage to " + targetName, (int) amount);
    }

    // Record damage taken
    public void addDamageTaken(String attackerName, double amount) {
        addActionEvent("Damage", "Damage received from " + attackerName, (int) amount);
    }

    // Record mining (diamond, gold, iron, lapis lazuli, quartz)
    public void addMiningEvent(String type, int amount) {
        addActionEvent("Mining", type + " mined", amount);
    }

    // Increase experience gained
    public void addExpGained(int amount) {
        this.expGained += amount;
    }

    // Record golden apple consumed
    public void addGoldenAppleConsumed() {
        this.goldenApplesConsumed++;
    }

    // Record Golden Head consumed
    public void addGoldenHeadConsumed() {
        this.goldenHeadsConsumed++;
    }

    // Record an arrow shot
    public void addArrowShot() {
        this.arrowsShot++;
    }

    // Record an arrow hit
    public void addArrowHit() {
        this.arrowsHit++;
    }

    // Record that the player has ridden a horse
    public void addHorseRiding() {
        this.timesRiddenHorse++;
        addActionEvent("Mount", "Player has ridden a horse", 1);
    }

    // Add an action event to the list
    public void addActionEvent(String actionType, String detail, int amount) {
        actionEvents.add(new ActionEvent(actionType, detail, amount));
    }

    // Get summary of all actions formatted
    public String getActionSummary() {
        StringBuilder summary = new StringBuilder("Action history:\n");
        for (ActionEvent event : actionEvents) {
            summary.append(event.toString()).append("\n");
        }
        return summary.toString();
    }

    // Get summary of non-action statistics
    public String getNonActionSummary() {
        return "Experience gained: " + expGained + "\n" +
                "Golden apples consumed: " + goldenApplesConsumed + "\n" +
                "Golden Heads consumed: " + goldenHeadsConsumed + "\n" +
                "Arrows shot: " + arrowsShot + "\n" +
                "Arrows hit: " + arrowsHit + "\n" +
                "Times ridden horse: " + timesRiddenHorse;
    }
}
