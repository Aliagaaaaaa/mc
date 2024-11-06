package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class DiamondLess  implements Scenario{
    @Override
    public String getName() {
        return "DiamondLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "dl";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND;
    }

    @Override
    public void unregister() {

    }
}
