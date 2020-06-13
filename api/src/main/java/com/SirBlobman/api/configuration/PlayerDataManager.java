package com.SirBlobman.api.configuration;

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

public final class PlayerDataManager<P extends JavaPlugin> {
    private final P plugin;
    private final Map<UUID, YamlConfiguration> configurationMap;
    public PlayerDataManager(P plugin) {
        this.plugin = plugin;
        this.configurationMap = new HashMap<>();
    }
    
    public final P getPlugin() {
        return this.plugin;
    }
    
    public final YamlConfiguration getData(OfflinePlayer player) {
        try {
            UUID uuid = player.getUniqueId();
            YamlConfiguration config = this.configurationMap.getOrDefault(uuid, null);
            if(config == null) {
                config = loadData(player);
                this.configurationMap.put(uuid, config);
            }
            
            return config;
        } catch(IOException | InvalidConfigurationException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while getting a user config file:", ex);
            return new YamlConfiguration();
        }
    }
    
    public final void saveData(OfflinePlayer player) {
        try {
            File dataFolder = this.plugin.getDataFolder();
            if(!dataFolder.exists()) {
                boolean makeFolder = dataFolder.mkdirs();
                if(!makeFolder) {
                    Logger logger = this.plugin.getLogger();
                    logger.warning("The plugin folder could not be made!");
                    return;
                }
            }
            
            File playerDataFolder = new File(dataFolder, "player-data");
            if(!playerDataFolder.exists()) {
                boolean makeFolder = playerDataFolder.mkdirs();
                if(!makeFolder) {
                    Logger logger = this.plugin.getLogger();
                    logger.warning("The player data folder could not be made!");
                    return;
                }
            }
            
            File file = getFile(player);
            if(!file.exists()) {
                boolean makeFile = file.createNewFile();
                if(!makeFile) {
                    Logger logger = this.plugin.getLogger();
                    logger.warning("The file named '" + file + "' could not be created!");
                    return;
                }
            }
            
            YamlConfiguration config = getData(player);
            config.save(file);
        } catch(IOException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while getting a user config file:", ex);
        }
    }
    
    public boolean hasData(OfflinePlayer player) {
        File file = getFile(player);
        return file.exists();
    }
    
    private File getFile(OfflinePlayer player) {
        File dataFolder = this.plugin.getDataFolder();
        File playerDataFolder = new File(dataFolder, "player-data");
    
        UUID uuid = player.getUniqueId();
        String uuidString = uuid.toString();
        String fileName = (uuidString + ".data.yml");
        return new File(playerDataFolder, fileName);
    }
    
    private YamlConfiguration loadData(OfflinePlayer player) throws IOException, InvalidConfigurationException {
        File file = getFile(player);
        if(!file.exists()) return new YamlConfiguration();
        
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        return config;
    }
}