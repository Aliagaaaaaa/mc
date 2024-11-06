package lol.aliaga.nuhc.game;

import lol.aliaga.nuhc.scenarios.Scenario;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class GameConfig {

    private int teams; //ffa=1, teams=2+

    private int border;
    private int netherBorder;

    private int pvpTime;

    private int finalHealTime;

    private boolean enderpearl;

    private int speed; // 0 - desactivada, 1 - hasta speed1 , 2 - hasta speed 2

    private int strength; // 0 - desactivada, 1 - hasta strength1 , 2 - hasta strength2

    private int poison; // 0 - desactivada, 1 - hasta poison1, 2 - hasta poison2

    private float appleRate;

    private boolean nether;

    private int borderShrinking; //3000 - 2000 - 1500 - 1000 - 500 - 250 - 100 - 50

    private float flintRate;

    private int maxPlayers;

    private boolean practice;

    private List<Scenario> scenarios;

    public GameConfig(){
        //default values
        this.teams = 1;
        this.netherBorder = 1000;
        this.border = 2000;
        this.pvpTime = 20;
        this.finalHealTime = 10;
        this.enderpearl = true;
        this.speed = 2;
        this.strength = 0;
        this.poison = 2;
        this.appleRate = 1;
        this.nether = true;
        this.borderShrinking = 45;
        this.flintRate = 1;
        this.maxPlayers = 100;
        this.scenarios = new ArrayList<>();
    }

    public void removeScenario(Scenario scenario) {
        scenarios.remove(scenario);
    }


    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }


}
