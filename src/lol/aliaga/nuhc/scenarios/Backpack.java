package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class Backpack implements Scenario {


    @Override
    public String getName() {
        return "Backpack";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "bp";
    }

    @Override
    public Material getIcon() {
        return Material.CHEST;
    }

    @Override
    public void unregister() {

    }
}
