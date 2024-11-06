package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class BowLess  implements Scenario{
    @Override
    public String getName() {
        return "BowLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "bl";
    }

    @Override
    public Material getIcon() {
        return Material.BOW;
    }

    @Override
    public void unregister() {

    }
}
