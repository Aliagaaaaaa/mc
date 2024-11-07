package lol.aliaga.nuhc.player.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UHCStats {

    private final Map<StatType, Integer> stats;
    private final List<ActionEvent> actionEvents;

    public UHCStats() {
        this.stats = new HashMap<>();
        this.actionEvents = new ArrayList<>();
        for (StatType statType : StatType.values()) {
            stats.put(statType, 0);
        }
    }

    public void incrementStat(StatType statType, int amount) {
        stats.put(statType, stats.getOrDefault(statType, 0) + amount);
    }

    public int getStat(StatType statType) {
        return stats.getOrDefault(statType, 0);
    }

    public void recordAction(String actionType, String detail, int amount) {
        actionEvents.add(new ActionEvent(actionType, detail, amount));
    }

    public String getActionSummary() {
        return actionEvents.stream()
                .map(ActionEvent::toString)
                .collect(Collectors.joining("\n"));
    }

    public String getStatSummary() {
        return stats.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
}
