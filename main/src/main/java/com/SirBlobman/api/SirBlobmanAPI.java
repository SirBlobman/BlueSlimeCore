package com.SirBlobman.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.nms.NMS_Handler;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;

public class SirBlobmanAPI {
    private final JavaPlugin plugin;
    private final Logger logger;
    public SirBlobmanAPI(JavaPlugin plugin) {
        Validate.notNull(plugin, "plugin cannot be NULL!");
        
        this.plugin = plugin;
        this.logger = Logger.getLogger("SirBlobmanAPI - " + plugin.getName());
    }
    
    public static SirBlobmanAPI getInstance(JavaPlugin plugin) {
        return new SirBlobmanAPI(plugin);
    }
    
    /**
     * @return The handler for all NMS tasks (boss bar, action bar, tab)
     */
    public NMS_Handler getHandlerNMS() {
        return NMS_Handler.getHandler();
    }
    
    /**
     * @param player The player to get the file for
     * @return The data yml file for {@code player}
     */
    public YamlConfiguration getDataFile(OfflinePlayer player) {
        if(player == null) return null;
        
        UUID uuid = player.getUniqueId();
        String uuidString = uuid.toString();
        String fileName = uuidString + ".yml";
        return getConfig("player data/" + fileName);
    }

    /**
     * @param player The player to save these values for
     * @param dataFile the YamlConfiguration to save
     */
    public void saveDataFile(OfflinePlayer player, YamlConfiguration dataFile) {
        if(player == null || dataFile == null) return;

        UUID uuid = player.getUniqueId();
        String uuidString = uuid.toString();
        String fileName = uuidString + ".yml";
        saveConfig("player data/" + fileName, dataFile);
    }

    /**
     * @param fileName The name of the file to get data from (use '/' to indicate paths)
     * @return The config data from {@code fileName}
     */
    public YamlConfiguration getConfig(String fileName) {
        try {
            File folder = plugin.getDataFolder();
            fileName = fileName.replace('/', File.separatorChar);
            File file = new File(folder, fileName);
            if(!file.exists()) {
                folder.mkdirs();
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            return config;
        } catch(IOException | InvalidConfigurationException ex) {
            String error = "An error occurred while loading a config with name '" + fileName + "'. An empty config will be returned...";
            this.logger.log(Level.WARNING, error, ex);
            return new YamlConfiguration();
        }
    }

    /**
     * @param fileName The name of the file to save to
     * @param config The YamlConfiguration to save
     */
    public void saveConfig(String fileName, YamlConfiguration config) {
        try {
            File folder = plugin.getDataFolder();
            fileName = fileName.replace('/', File.separatorChar);
            File file = new File(folder, fileName);
            if(!file.exists()) {
                folder.mkdirs();
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            config.save(file);
        } catch(IOException ex) {
            String error = "An error ocurred while saving a config with name '" + fileName + "'.";
            this.logger.log(Level.WARNING, error, ex);
        }
    }
}