package lol.aliaga.nuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.event.Listener;

public interface Scenario extends Listener {

    String getName();
    String information();
    String abreviation();
    Material getIcon();
    void unregister();



}
