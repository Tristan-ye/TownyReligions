package dev.goodrich.pantheon.listeners;

import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import dev.goodrich.pantheon.util.ReligionCalculator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownChangeListener implements Listener {

    @EventHandler
    public void onTownAddResident(TownAddResidentEvent event) {
            ReligionCalculator.refreshTown(event.getTown());
    }

    @EventHandler
    public void onTownRemoveResident(TownRemoveResidentEvent event) {
        ReligionCalculator.refreshTown(event.getTown());
    }

    @EventHandler
    public void onTownDelete(DeleteTownEvent event) {
        ReligionCalculator.invalidateTown(event.getTownName());
    }
}
