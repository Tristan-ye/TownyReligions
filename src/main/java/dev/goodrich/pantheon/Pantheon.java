package dev.goodrich.pantheon;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.KeyAlreadyRegisteredException;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import dev.goodrich.pantheon.commands.*;
import dev.goodrich.pantheon.data.ReligionData;
import dev.goodrich.pantheon.listeners.NationChangeListener;
import dev.goodrich.pantheon.listeners.ReligionStatusScreenListener;
import dev.goodrich.pantheon.listeners.TownChangeListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public final class Pantheon extends JavaPlugin {

    private List<String> allowedReligions;
    private static Pantheon instance;
    public static final String RELIGION_METADATA_KEY = "pantheon_religion";
    private static final StringDataField RELIGION_METADATA_FIELD = new StringDataField(RELIGION_METADATA_KEY, "", "Religion");

    /**
     * Get the instance of this plugin.
     * @return instance of Pantheon.
     */
    public static Pantheon getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Load religions from configuration.
        loadReligionConfig();

        try {
            TownyAPI.getInstance().registerCustomDataField(RELIGION_METADATA_FIELD);
            getLogger().info("Pantheon religion metadata field registered successfully.");
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning("Religion metadata key already registered: " + e.getMessage());
        }

        // Register reload command for /pantheon reload religions.
        getCommand("pantheon").setExecutor(new PantheonReloadCommand());

        // Initialize and register the resident set religion command.
        // The command registers itself with TownyCommandAddonAPI.
        new SetReligionCommand();
        new ResidentReligionAddon();
        new TownReligionAddon();
        new NationReligionAddon();

        getServer().getPluginManager().registerEvents(new ReligionStatusScreenListener(), this);
        getServer().getPluginManager().registerEvents(new NationChangeListener(), this);
        getServer().getPluginManager().registerEvents(new TownChangeListener(), this);

    }

    /**
     * Reload the religions configuration.
     * @return true if reload was successful, false otherwise.
     */
    public boolean reloadReligions() {
        try {
            ReligionData.clearCache();
            loadReligionConfig();
            getLogger().log(Level.INFO, "Religions reloaded successfully.");
            return true;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error reloading religions configuration.", e);
            return false;
        }
    }

    /**
     * Loads the allowed religions from the religion.yml file.
     */
    private void loadReligionConfig() {
        // Ensure the plugin's data folder exists.
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File file = new File(getDataFolder(), "religion.yml");
        if (!file.exists()) {
            saveResource("religion.yml", false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        allowedReligions = config.getStringList("religions");
        getLogger().info("Allowed religions loaded: " + allowedReligions);
    }

    /**
     * Returns the current list of allowed religions.
     * @return a list of allowed religion names.
     */
    public List<String> getAllowedReligions() {
        return allowedReligions;
    }
}
