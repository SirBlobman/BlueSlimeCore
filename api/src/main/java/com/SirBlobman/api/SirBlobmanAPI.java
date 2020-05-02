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
        this.logger.info("This plugin is using SirBlobmanAPI.");
        
        String[] message = {
                "  ___ _     ___ _     _                    ",
                " / __(_)_ _| _ | |___| |__ _ __  __ _ _ _  ",
                " \\__ | | '_| _ | / _ | '_ | '  \\/ _` | ' \\ ",
                " |___|_|_| |___|_\\___|_.__|_|_|_\\__,_|_||_|",
                "                                           "
        };
        Arrays.stream(message).forEach(this.logger::info);
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    /**
     * @deprecated
     * @see com.SirBlobman.api.configuration.ConfigManager
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
     * @see com.SirBlobman.api.configuration.ConfigManager
     */
    public YamlConfiguration getConfig(String fileName) {
        File pluginFolder = this.plugin.getDataFolder();
        File file = new File(pluginFolder, fileName);
        return getConfig(file);
    }
    
    /**
     * @deprecated
     * @see com.SirBlobman.api.configuration.ConfigManager
     */
    public void saveConfig(String fileName, FileConfiguration config) {
        File pluginFolder = this.plugin.getDataFolder();
        File file = new File(pluginFolder, fileName);
        saveConfig(file, config);
    }
    
    /**
     * @deprecated
     * @see com.SirBlobman.api.configuration.ConfigManager
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
     * @deprecated
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
