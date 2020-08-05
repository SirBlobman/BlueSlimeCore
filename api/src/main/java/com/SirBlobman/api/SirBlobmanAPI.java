package com.SirBlobman.api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SirBlobmanAPI {
    public static SirBlobmanAPI getInstance(JavaPlugin plugin) {
        return new SirBlobmanAPI(plugin);
    }
    
    private final JavaPlugin plugin;
    private final Logger logger;
    public SirBlobmanAPI(JavaPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.logger = this.plugin.getLogger();
        this.logger.info("This plugin is using SirBlobmanAPI by SirBlobman.");
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    public void sendLogo() {
        String[] logoArray = {
                "  ___ _     ___ _     _                    ",
                " / __(_)_ _| _ | |___| |__ _ __  __ _ _ _  ",
                " \\__ | | '_| _ | / _ | '_ | '  \\/ _` | ' \\ ",
                " |___|_|_| |___|_\\___|_.__|_|_|_\\__,_|_||_|",
                "                                           "
        };
        Logger logger = getLogger();
        for(String message : logoArray) logger.info(message);
    }
    
    /**
     * @deprecated
     * @param player The Player that owns this data
     * @see com.SirBlobman.api.configuration.ConfigManager
     * @return The data file for the player
     */
    public YamlConfiguration getDataFile(OfflinePlayer player) {
        if(player == null) return null;
    
        UUID uuid = player.getUniqueId();
        String uuidString = uuid.toString();
        String fileName = (uuidString + ".yml");
        
        File pluginFolder = this.plugin.getDataFolder();
        File dataFolder = new File(pluginFolder, "player-data");
        
        File file = new File(dataFolder, fileName);
        return getConfig(file);
    }
    
    /**
     * @deprecated
     * @param player The Player that owns this data
     * @param dataFile The data that will be saved
     * @see com.SirBlobman.api.configuration.ConfigManager
     */
    public void saveDataFile(OfflinePlayer player, YamlConfiguration dataFile) {
        if(player == null || dataFile == null) return;
    
        UUID uuid = player.getUniqueId();
        String uuidString = uuid.toString();
        String fileName = (uuidString + ".yml");
    
        File pluginFolder = this.plugin.getDataFolder();
        File dataFolder = new File(pluginFolder, "player-data");
    
        File file = new File(dataFolder, fileName);
        saveConfig(file, dataFile);
    }
    
    /**
     * @deprecated
     * @param fileName the name of the file in the plugin folder/jar
     * @see com.SirBlobman.api.configuration.ConfigManager
     * @return A config file
     */
    public YamlConfiguration getConfig(String fileName) {
        File pluginFolder = this.plugin.getDataFolder();
        File file = new File(pluginFolder, fileName);
        return getConfig(file);
    }
    
    /**
     * @deprecated
     * @param fileName The name of the file that the contents will be saved in.
     * @param config The FileConfiguration to be saved
     * @see com.SirBlobman.api.configuration.ConfigManager
     */
    public void saveConfig(String fileName, FileConfiguration config) {
        File pluginFolder = this.plugin.getDataFolder();
        File file = new File(pluginFolder, fileName);
        saveConfig(file, config);
    }
    
    /**
     * @deprecated
     * @param file The file to load the config from if its not cached
     * @see com.SirBlobman.api.configuration.ConfigManager
     * @return a config file
     */
    public YamlConfiguration getConfig(File file) {
        try {
            File realFile = file.getCanonicalFile();
            if(!realFile.exists()) {
                File parent = realFile.getParentFile();
                if(!parent.exists()) {
                    boolean makeParent = parent.mkdirs();
                    if(!makeParent) {
                        Logger logger = getLogger();
                        logger.warning("Failed to make parent file for '" + realFile + "'.");
                        return new YamlConfiguration();
                    }
                }
                
                boolean makeFile = realFile.createNewFile();
                if(!makeFile) {
                    Logger logger = getLogger();
                    logger.warning("Failed to make file '" + realFile + "'.");
                    return new YamlConfiguration();
                }
            }
            
            YamlConfiguration config = new YamlConfiguration();
            config.load(realFile);
            return config;
        } catch(IOException | InvalidConfigurationException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while loading a config for file '" + file + "'. An empty config will be returned.", ex);
            return new YamlConfiguration();
        }
    }
    
    /**
     * @deprecated Use ConfigManager instead
     * @param file The file that the contents will be saved in.
     * @param config The FileConfiguration to be saved
     * @see com.SirBlobman.api.configuration.ConfigManager
     */
    public void saveConfig(File file, FileConfiguration config) {
        try {
            File realFile = file.getCanonicalFile();
            if(!realFile.exists()) {
                File parent = realFile.getParentFile();
                if(!parent.exists()) {
                    boolean makeParent = parent.mkdirs();
                    if(!makeParent) {
                        Logger logger = getLogger();
                        logger.warning("Failed to make parent file for '" + realFile + "'.");
                        return;
                    }
                }
    
                boolean makeFile = realFile.createNewFile();
                if(!makeFile) {
                    Logger logger = getLogger();
                    logger.warning("Failed to make file '" + realFile + "'.");
                    return;
                }
            }
            
            config.save(file);
        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while saving a config for file '" + file + "'.", ex);
        }
    }
}
