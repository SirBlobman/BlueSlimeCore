package com.SirBlobman.api.plugin;

import com.SirBlobman.api.SirBlobmanAPI;
import com.SirBlobman.api.configuration.ConfigManager;
import com.SirBlobman.api.configuration.PlayerDataManager;
import com.SirBlobman.api.nms.MultiVersionHandler;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SirBlobmanPlugin<P extends JavaPlugin> extends JavaPlugin {
    private final SirBlobmanAPI sirBlobmanAPI;
    private final MultiVersionHandler<?> multiVersionHandler;
    private final ConfigManager<?> configManager;
    private final PlayerDataManager<?> playerDataManager;
    
    public SirBlobmanPlugin() {
        this.sirBlobmanAPI = new SirBlobmanAPI(this);
        this.multiVersionHandler = new MultiVersionHandler<>(this);
        this.configManager = new ConfigManager<>(this);
        this.playerDataManager = new PlayerDataManager<>(this);
    }
    
    public final SirBlobmanAPI getSirBlobmanAPI() {
        return this.sirBlobmanAPI;
    }
    
    public final MultiVersionHandler<?> getMultiVersionHandler() {
        return this.multiVersionHandler;
    }
    
    public final ConfigManager<?> getConfigManager() {
        return this.configManager;
    }
    
    public final PlayerDataManager<?> getPlayerDataManager() {
        return this.playerDataManager;
    }
    
    @Override
    public void saveDefaultConfig() {
        ConfigManager<?> configManager = getConfigManager();
        configManager.saveDefaultConfig("config.yml");
    }
    
    @Override
    public void reloadConfig() {
        ConfigManager<?> configManager = getConfigManager();
        configManager.reloadConfig("config.yml");
    }
    
    @Override
    public YamlConfiguration getConfig() {
        ConfigManager<?> configManager = getConfigManager();
        return configManager.getConfig("config.yml");
    }
    
    @Override
    public void saveConfig() {
        ConfigManager<?> configManager = getConfigManager();
        configManager.saveConfig("config.yml");
    }
    
    @Override public abstract void onLoad();
    @Override public abstract void onEnable();
    @Override public abstract void onDisable();
}