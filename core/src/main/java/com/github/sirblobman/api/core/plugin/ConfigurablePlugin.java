package com.github.sirblobman.api.core.plugin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.MultiVersionHandler;

public abstract class ConfigurablePlugin extends JavaPlugin {
    private final ConfigurationManager configurationManager;
    private final MultiVersionHandler multiVersionHandler;
    private final PlayerDataManager playerDataManager;
    private final LanguageManager languageManager;

    public ConfigurablePlugin() {
        this.configurationManager = new ConfigurationManager(this);
        this.multiVersionHandler = new MultiVersionHandler(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.languageManager = new LanguageManager(this.configurationManager);
    }

    @Override
    public abstract void onLoad();

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();

    @Override
    public final void saveDefaultConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");
    }

    @Override
    public final void saveConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.save("config.yml");
    }

    @Override
    public final void reloadConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");
    }

    @Override
    public final YamlConfiguration getConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        return configurationManager.get("config.yml");
    }

    public final ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public final MultiVersionHandler getMultiVersionHandler() {
        return this.multiVersionHandler;
    }

    public final PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

    public final LanguageManager getLanguageManager() {
        return this.languageManager;
    }
}