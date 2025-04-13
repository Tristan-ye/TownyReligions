package dev.goodrich.pantheon.util;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.goodrich.pantheon.data.ReligionData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReligionCalculator {
    // Cache duration in milliseconds (e.g. 60 seconds)
    private static final long CACHE_DURATION = 60000;

    // Separate caches for town and nation breakdowns
    private static final Map<String, CachedBreakdown> townCache = new ConcurrentHashMap<>();
    private static final Map<String, CachedBreakdown> nationCache = new ConcurrentHashMap<>();

    public static String getTownBreakdown(Town town) {
        String key = town.getName();
        CachedBreakdown cached = townCache.get(key);
        long now = System.currentTimeMillis();
        if (cached != null && (now - cached.timestamp < CACHE_DURATION)) {
            return cached.breakdown;
        }
        // Calculate breakdown from residents
        Map<String, Integer> counts = new HashMap<>();
        int total = 0;
        for (Resident resident : town.getResidents()) {
            String religion = ReligionData.getResidentReligion(resident.getUUID());
            counts.put(religion, counts.getOrDefault(religion, 0) + 1);
            total++;
        }
        String breakdown = formatBreakdown(counts, total);
        townCache.put(key, new CachedBreakdown(breakdown, now));
        return breakdown;
    }

    public static String getNationBreakdown(Nation nation) {
        String key = nation.getName();
        CachedBreakdown cached = nationCache.get(key);
        long now = System.currentTimeMillis();
        if (cached != null && (now - cached.timestamp < CACHE_DURATION)) {
            return cached.breakdown;
        }
        Map<String, Integer> counts = new HashMap<>();
        int total = 0;
        // Iterate through all towns in the nation.
        for (Town town : nation.getTowns()) {
            for (Resident resident : town.getResidents()) {
                String religion = ReligionData.getResidentReligion(resident.getUUID());
                counts.put(religion, counts.getOrDefault(religion, 0) + 1);
                total++;
            }
        }
        String breakdown = formatBreakdown(counts, total);
        nationCache.put(key, new CachedBreakdown(breakdown, now));
        return breakdown;
    }

    private static String formatBreakdown(Map<String, Integer> counts, int total) {
        StringBuilder sb = new StringBuilder();
        if (total == 0) {
            return "No data";
        }
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double percent = (entry.getValue() * 100.0) / total;
            sb.append(entry.getKey())
                    .append(": ")
                    .append(String.format("%.0f", percent))
                    .append("%, ");
        }
        // Remove trailing comma and space.
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }

    // Simple cached breakdown structure.
    private static class CachedBreakdown {
        String breakdown;
        long timestamp;
        CachedBreakdown(String breakdown, long timestamp) {
            this.breakdown = breakdown;
            this.timestamp = timestamp;
        }
    }

    public static void invalidateNation(String name) {
        nationCache.remove(name);
    }

    public static void invalidateTown(String name) {
        townCache.remove(name);
    }

    public static void updateTownCache(Town town) {
        String breakdown = getTownBreakdown(town); // this will automatically recache
        townCache.put(town.getName(), new CachedBreakdown(breakdown, System.currentTimeMillis()));
    }

    public static void refreshTown(Town town) {
        invalidateTown(town.getName());
        updateTownCache(town);
    }


    // Optionally, call this method when religion data changes (e.g. on reload).
    public static void clearCaches() {
        townCache.clear();
        nationCache.clear();
    }
}
