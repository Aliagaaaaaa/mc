package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class IronLess  implements Scenario{
    @Override
    public String getName() {
        return "IronLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "il";
    }

    @Override
    public Material getIcon() {
        return Material.IRON_INGOT;
    }

    @Override
    public void unregister() {

    }
}
