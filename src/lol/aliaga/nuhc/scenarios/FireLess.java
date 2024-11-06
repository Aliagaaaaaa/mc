package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class FireLess  implements Scenario{
    @Override
    public String getName() {
        return "FireLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "fl";
    }

    @Override
    public Material getIcon() {
        return Material.FLINT_AND_STEEL;
    }

    @Override
    public void unregister() {

    }
}
