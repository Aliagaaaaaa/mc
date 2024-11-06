package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class RodLess  implements Scenario{
    @Override
    public String getName() {
        return "RodLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "rl";
    }

    @Override
    public Material getIcon() {
        return Material.FISHING_ROD;
    }

    @Override
    public void unregister() {

    }
}
