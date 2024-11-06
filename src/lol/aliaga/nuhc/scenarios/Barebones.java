package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class Barebones  implements Scenario{
    @Override
    public String getName() {
        return "Barebones";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "bb";
    }

    @Override
    public Material getIcon() {
        return Material.BONE;
    }

    @Override
    public void unregister() {

    }
}
