package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class HorseLess  implements Scenario{
    @Override
    public String getName() {
        return "HorseLess";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "hl";
    }

    @Override
    public Material getIcon() {
        return Material.SADDLE;
    }

    @Override
    public void unregister() {

    }
}
