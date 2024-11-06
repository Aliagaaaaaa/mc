package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class BloodDiamond  implements Scenario{
    @Override
    public String getName() {
        return "BloodDiamond";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "bd";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND;
    }

    @Override
    public void unregister() {

    }
}
