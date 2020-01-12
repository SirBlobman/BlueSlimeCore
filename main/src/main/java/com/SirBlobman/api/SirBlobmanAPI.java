package com.SirBlobman.api;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.api.nms.NMS_Handler;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.lang.Validate;

public class SirBlobmanAPI {
    public static SirBlobmanAPI getInstance(JavaPlugin plugin) {
        return new SirBlobmanAPI(plugin);
    }
    
    private final JavaPlugin plugin;
    private final Logger logger;
    public SirBlobmanAPI(JavaPlugin plugin) {
        Validate.notNull(plugin, "plugin must not be NULL!");
        this.plugin = plugin;
        
        Logger logger = this.plugin.getLogger();
        Logger subLogger = Logger.getLogger("SirBlobmanAPI");
        subLogger.setParent(logger);
        this.logger = subLogger;
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    public NMS_Handler getVersionHandler() {
        return NMS_Handler.getHandler();
    }
    
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
    
    public YamlConfiguration getConfig(String fileName) {
        File pluginFolder = this.plugin.getDataFolder();
        File file = new File(pluginFolder, fileName);
        return getConfig(file);
    }
    
    public void saveConfig(String fileName, FileConfiguration config) {
        File pluginFolder = this.plugin.getDataFolder();
        File file = new File(pluginFolder, fileName);
        saveConfig(file, config);
    }
    
    public YamlConfiguration getConfig(File file) {
        try {
            File realFile = file.getCanonicalFile();
            if(!realFile.exists()) {
                File parent = realFile.getParentFile();
                parent.mkdirs();
                
                realFile.createNewFile();
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
    
    public void saveConfig(File file, FileConfiguration config) {
        try {
            File realFile = file.getCanonicalFile();
            if(!realFile.exists()) {
                File parent = realFile.getParentFile();
                parent.mkdirs();
        
                realFile.createNewFile();
            }
            
            config.save(file);
        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while saving a config for file '" + file + "'.", ex);
        }
    }
}
