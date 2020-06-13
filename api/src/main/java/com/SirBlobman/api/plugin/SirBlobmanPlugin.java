package com.SirBlobman.api.plugin;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.api.SirBlobmanAPI;
import com.SirBlobman.api.command.CustomCommand;
import com.SirBlobman.api.configuration.ConfigManager;
import com.SirBlobman.api.configuration.PlayerDataManager;
import com.SirBlobman.api.nms.MultiVersionHandler;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.Listener;

public abstract class SirBlobmanPlugin<P> extends JavaPlugin implements ConfigPlugin {
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
    
    protected final void registerCommand(Class<? extends CustomCommand<?>> commandClass) {
        try {
            Class<?> thisClass = getClass();
            Constructor<? extends CustomCommand<?>> constructor = commandClass.getDeclaredConstructor(thisClass);
            
            CustomCommand<?> customCommand = constructor.newInstance(this);
            customCommand.register();
        } catch(Exception ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while registering a command:", ex);
        }
    }
    
    protected final void registerListener(Listener listener) {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(listener, this);
    }
    
    @Override public abstract void onLoad();
    @Override public abstract void onEnable();
    @Override public abstract void onDisable();
}