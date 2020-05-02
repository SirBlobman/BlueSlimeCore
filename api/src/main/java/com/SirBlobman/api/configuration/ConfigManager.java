package com.SirBlobman.api.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.api.utility.MessageUtil;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager<P extends JavaPlugin> {
    private final P plugin;
    private final Map<String, YamlConfiguration> fileNameToConfigMap = new HashMap<>();
    public ConfigManager(P plugin) {
        this.plugin = plugin;
    }
    
    public void saveDefaultConfig(String fileName) {
        if(fileName == null) return;
        
        File actualFile = getActualFile(fileName);
        if(actualFile.exists()) return;
        
        InputStream inputStream = this.plugin.getResource(fileName);
        if(inputStream == null) {
            Logger logger = this.plugin.getLogger();
            logger.warning("Could not find a default config file named '" + fileName + " in the jar.");
            return;
        }
        
        try {
            File parentFile = actualFile.getParentFile();
            if(parentFile != null && !parentFile.exists()) {
                boolean makeParent = parentFile.mkdirs();
                if(!makeParent) {
                    Logger logger = this.plugin.getLogger();
                    logger.warning("Failed to make the parent folder for '" + fileName + "'.");
                    return;
                }
            }
    
            boolean makeFile = actualFile.createNewFile();
            if(!makeFile) {
                Logger logger = this.plugin.getLogger();
                logger.warning("Failed to make the file '" + fileName + "'.");
                return;
            }
            
            Path path = actualFile.toPath();
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while saving the default config for '" + actualFile + "':", ex);
        }
    }
    
    public YamlConfiguration getConfig(String fileName) {
        if(fileName == null) return new YamlConfiguration();
        
        File actualFile = getActualFile(fileName);
        String actualFileName = actualFile.getAbsolutePath();
        
        YamlConfiguration config = this.fileNameToConfigMap.getOrDefault(actualFileName, null);
        if(config == null) {
            reloadConfig(fileName);
            config = this.fileNameToConfigMap.getOrDefault(actualFileName, new YamlConfiguration());
        }
        
        return config;
    }
    
    public void reloadConfigs() {
        Map<String, YamlConfiguration> copyMap = new HashMap<>(this.fileNameToConfigMap);
        Set<String> keySet = copyMap.keySet();
        
        for(String actualFileName : keySet) {
            File actualFile = getActualFile(actualFileName);
            String fileName = actualFile.getName();
            reloadConfig(fileName);
        }
    }
    
    public void reloadConfig(String fileName) {
        if(fileName == null) return;
        
        File actualFile = getActualFile(fileName);
        String actualFileName = actualFile.getAbsolutePath();
        if(!actualFile.exists()) return;
        
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(actualFile);
            
            InputStream inputStream = this.plugin.getResource(fileName);
            if(inputStream == null) {
                this.fileNameToConfigMap.put(actualFileName, config);
                return;
            }
            
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            YamlConfiguration defaultConfig = new YamlConfiguration();
            defaultConfig.load(reader);
            
            config.setDefaults(defaultConfig);
            this.fileNameToConfigMap.put(actualFileName, config);
        } catch(IOException | InvalidConfigurationException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while loading the config for '" + fileName + "'.", ex);
        }
    }
    
    public void saveConfig(String fileName) {
        if(fileName == null) return;
        
        File actualFile = getActualFile(fileName);
        YamlConfiguration config = getConfig(fileName);
        
        try {
            config.save(actualFile);
        } catch(IOException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while saving the config for '" + fileName + "'.", ex);
        }
    }
    
    public String getConfigMessage(String fileName, String path, boolean translateColorCodes) {
        if(fileName == null || path == null || path.isEmpty()) return "";
        
        YamlConfiguration config = getConfig(fileName);
        if(config == null) return "{" + path + "}";
        
        if(config.isList(path)) {
            List<String> stringList = config.getStringList(path);
            String string = String.join("\n", stringList);
            
            return (translateColorCodes ? MessageUtil.color(string) : string);
        }
        
        if(config.isString(path)) {
            String string = config.getString(path);
            if(string == null) return "{" + path + "}";
            
            return (translateColorCodes ? MessageUtil.color(string) : string);
        }
        
        return "{" + path + "}";
    }
    
    private File getActualFile(String fileName) {
        if(fileName == null || fileName.isEmpty()) return null;
        
        File pluginFolder = this.plugin.getDataFolder();
        File file = new File(pluginFolder, fileName);
        return file.getAbsoluteFile();
    }
}
