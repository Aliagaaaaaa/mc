package lol.aliaga.nuhc.game;

import lombok.Getter;
import lombok.Setter;
import lol.aliaga.nuhc.scenarios.Scenario;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameConfig {

    private int teams = 1; // FFA = 1, teams = 2+
    private int border = 2000;
    private int netherBorder = 1000;
    private int pvpTime = 20;
    private int finalHealTime = 10;
    private boolean enderpearl = true;
    private int speed = 2; // 0 = off, 1 = up to Speed 1, 2 = up to Speed 2
    private int strength = 0; // 0 = off, 1 = up to Strength 1, 2 = up to Strength 2
    private int poison = 2; // 0 = off, 1 = up to Poison 1, 2 = up to Poison 2
    private float appleRate = 1;
    private boolean nether = true;
    private int borderShrinking = 45; // Shrink times (in minutes)
    private float flintRate = 1;
    private int maxPlayers = 100;
    private boolean practice = false;
    private List<Scenario> scenarios = new ArrayList<>();

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public void removeScenario(Scenario scenario) {
        scenarios.remove(scenario);
    }
}
