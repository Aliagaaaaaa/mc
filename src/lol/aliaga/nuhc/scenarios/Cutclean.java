package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class Cutclean  implements Scenario{
    @Override
    public String getName() {
        return "Cutclean";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "cc";
    }

    @Override
    public Material getIcon() {
        return Material.IRON_INGOT;
    }

    @Override
    public void unregister() {

    }
}
