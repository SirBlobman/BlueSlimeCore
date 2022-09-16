package com.github.sirblobman.api.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

public final class PlayerDataManager {
    private final JavaPlugin plugin;
    private final Map<UUID, YamlConfiguration> configurationMap;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.configurationMap = new HashMap<>();
    }

    /**
     * Fetch the data configuration for the specified player.
     * If the player does not have any data, the configuration will be empty.
     *
     * @param player The player who owns the configuration
     * @return A configuration from memory. If the configuration is not in memory it will be loaded from a file.
     */
    public YamlConfiguration get(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        YamlConfiguration configuration = this.configurationMap.getOrDefault(playerId, null);
        if (configuration != null) {
            return configuration;
        }

        reload(player);
        return get(player);
    }

    /**
     * Saves a player configuration to a file
     *
     * @param player The player who owns the configuration
     */
    public void save(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        YamlConfiguration configuration = this.configurationMap.getOrDefault(playerId, null);
        if (configuration == null) {
            return;
        }

        try {
            File file = getFile(player);
            configuration.save(file);
        } catch (IOException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "Failed to save data for player '" + playerId
                    + "' because an error occurred:", ex);
        }
    }

    /**
     * Reloads a player configuration from a file into memory
     *
     * @param player The player who owns the configuration
     */
    public void reload(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        try {
            YamlConfiguration configuration = load(player);
            this.configurationMap.put(playerId, configuration);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "Failed to load data for player '" + playerId
                    + "' because an error occurred:", ex);
        }
    }

    /**
     * Check if the specified player has any data.
     *
     * @param player The player to check
     * @return {@code true} if the data file exists, otherwise {@code false}.
     * @see File#exists()
     */
    public boolean hasData(OfflinePlayer player) {
        File playerFile = getFile(player);
        return playerFile.exists();
    }

    private File getFile(OfflinePlayer player) {
        File dataFolder = this.plugin.getDataFolder();
        File playerDataFolder = new File(dataFolder, "playerdata");

        UUID playerId = player.getUniqueId();
        String playerIdString = playerId.toString();
        String fileName = (playerIdString + ".data.yml");
        return new File(playerDataFolder, fileName);
    }

    private YamlConfiguration load(OfflinePlayer player) throws IOException, InvalidConfigurationException {
        File playerFile = getFile(player);
        if (!playerFile.exists()) {
            return new YamlConfiguration();
        }

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(playerFile);
        return configuration;
    }
}
