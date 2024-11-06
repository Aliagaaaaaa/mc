package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;

public class BloodEnchant implements Scenario{
    @Override
    public String getName() {
        return "BloodEnchant";
    }

    @Override
    public String information() {
        return "";
    }

    @Override
    public String abreviation() {
        return "be";
    }

    @Override
    public Material getIcon() {
        return Material.ENCHANTMENT_TABLE;
    }

    @Override
    public void unregister() {

    }
}
