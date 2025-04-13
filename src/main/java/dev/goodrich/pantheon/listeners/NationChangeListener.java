package dev.goodrich.pantheon.listeners;

import com.palmergames.bukkit.towny.event.DeleteNationEvent;
import com.palmergames.bukkit.towny.event.NationAddTownEvent;
import com.palmergames.bukkit.towny.event.NationRemoveTownEvent;
import com.palmergames.bukkit.towny.object.Nation;
import dev.goodrich.pantheon.util.ReligionCalculator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NationChangeListener implements Listener {

    @EventHandler
    public void onTownAddedToNation(NationAddTownEvent event) {
        Nation nation = event.getNation();
        ReligionCalculator.invalidateNation(nation.getName());
    }

    @EventHandler
    public void onTownRemovedFromNation(NationRemoveTownEvent event) {
        Nation nation = event.getNation();
        ReligionCalculator.invalidateNation(nation.getName());
    }

    @EventHandler
    public void onNationDeleted(DeleteNationEvent event) {
        ReligionCalculator.invalidateNation(event.getNationName());
    }
}
