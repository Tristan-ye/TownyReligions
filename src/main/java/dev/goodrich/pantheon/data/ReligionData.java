package dev.goodrich.pantheon.data;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import dev.goodrich.pantheon.Pantheon;
import dev.goodrich.pantheon.util.ReligionCalculator;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ReligionData {
    // In-memory storage for resident religions mapped by their UUID.
    private static final Map<UUID, String> religionCache = new ConcurrentHashMap<>();

    /**
     * Set the religion for a given resident.
     *
     * @param uuid     the unique ID of the resident
     * @param religion the religion to set
     */
    public static void setResidentReligion(UUID uuid, String religion) {
        Resident resident = TownyAPI.getInstance().getResident(uuid);
        if (resident == null) return;

        // Remove any existing Religion metadata
        resident.removeMetaData(Pantheon.RELIGION_METADATA_KEY);

        // Now add the fresh one
        StringDataField field =
                new StringDataField(Pantheon.RELIGION_METADATA_KEY, religion, "Religion");
        resident.addMetaData(field);

        // Update cache & town breakdown as before…
        religionCache.put(uuid, religion);
        if (resident.hasTown())
            ReligionCalculator.refreshTown(resident.getTownOrNull());
    }


    /**
     * Retrieve the religion for a resident.
     *
     * @param uuid the unique ID of the resident
     * @return the religion, or "Unknown" if none is set
     */
    public static String getResidentReligion(UUID uuid) {
        // Fast path: cache hit
        if (religionCache.containsKey(uuid))
            return religionCache.get(uuid);

        // Slow path: load from metadata
        Resident resident = TownyAPI.getInstance().getResident(uuid);
        if (resident == null || !resident.hasMeta(Pantheon.RELIGION_METADATA_KEY))
            return "Unknown";

        StringDataField field = (StringDataField) resident.getMetadata(Pantheon.RELIGION_METADATA_KEY);
        String value = field.getValue().isEmpty() ? "Unknown" : field.getValue();

        // Cache it
        religionCache.put(uuid, value);
        return value;
    }

    public static void clearCache() {
        religionCache.clear();
    }
}
