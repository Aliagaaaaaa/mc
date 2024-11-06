package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class CoalLess  implements Scenario{
    @Override
    public String getName() {
        return "CoalLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "cl";
    }

    @Override
    public Material getIcon() {
        return Material.COAL;
    }

    @Override
    public void unregister() {

    }
}
