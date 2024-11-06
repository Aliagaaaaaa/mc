package lol.aliaga.nuhc.scenarios;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScenarioManager {

    private final List<Scenario> allScenarios;

    public ScenarioManager() {
        allScenarios = new ArrayList<>();
        allScenarios.add(new Backpack());
        allScenarios.add(new Barebones());
        allScenarios.add(new BloodDiamond());
        allScenarios.add(new BloodEnchant());
        allScenarios.add(new BowLess());
        allScenarios.add(new CoalLess());
        allScenarios.add(new Cutclean());
        allScenarios.add(new DiamondLess());
        allScenarios.add(new FireLess());
        allScenarios.add(new GoldLess());
        allScenarios.add(new HorseLess());
        allScenarios.add(new IronLess());
        allScenarios.add(new RodLess());
        allScenarios.add(new Timebomb());
    }

    public Scenario getScenario(String name) {
        for (Scenario scenario : allScenarios) {
            if (scenario.getName().equalsIgnoreCase(name)) {
                return scenario;
            }
        }
        return null;
    }


}