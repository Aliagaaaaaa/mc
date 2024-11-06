package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class GoldLess  implements Scenario{
    @Override
    public String getName() {
        return "GoldLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "gl";
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_INGOT;
    }

    @Override
    public void unregister() {

    }
}
