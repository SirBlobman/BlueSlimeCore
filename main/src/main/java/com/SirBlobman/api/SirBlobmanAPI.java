package com.SirBlobman.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.nms.NMS_Handler;

import java.io.File;
import java.util.UUID;
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
     * 
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
                file.createNewFile();
            }
            
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            return config;
        } catch(Exception ex) {
            this.logger.warning("An error has occurred getting a config with name '" + fileName + "'.");
            ex.printStackTrace();
            return new YamlConfiguration();
        }
    }
}