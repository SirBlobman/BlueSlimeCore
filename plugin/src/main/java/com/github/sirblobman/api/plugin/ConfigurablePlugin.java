package com.github.sirblobman.api.plugin;

import java.util.Locale;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.MultiVersionHandler;

public abstract class ConfigurablePlugin extends JavaPlugin implements IMultiVersionPlugin {
    private final ConfigurationManager configurationManager;
    private final MultiVersionHandler multiVersionHandler;
    private final PlayerDataManager playerDataManager;
    private final LanguageManager languageManager;

    private final FoliaHelper foliaHelper;

    public ConfigurablePlugin() {
        this.configurationManager = new ConfigurationManager(this);
        this.multiVersionHandler = new MultiVersionHandler(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.languageManager = new LanguageManager(this.configurationManager);
        this.foliaHelper = new FoliaHelper(this);
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
        reloadConfiguration();
    }

    @Override
    public final @NotNull YamlConfiguration getConfig() {
        ConfigurationManager configurationManager = getConfigurationManager();
        return configurationManager.get("config.yml");
    }

    @Override
    public @NotNull ConfigurablePlugin getPlugin() {
        return this;
    }

    @Override
    public @NotNull FoliaHelper getFoliaHelper() {
        return this.foliaHelper;
    }

    protected void reloadConfiguration() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");
    }

    public final @NotNull ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public final @NotNull MultiVersionHandler getMultiVersionHandler() {
        return this.multiVersionHandler;
    }

    public final @NotNull PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

    public final @NotNull LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public boolean isDebugMode() {
        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        return configuration.getBoolean("debug-mode", false);
    }

    public void printDebug(@NotNull String message) {
        if (isDebugMode()) {
            Logger logger = getLogger();
            String logMessage = String.format(Locale.US, "[Debug] %s", message);
            logger.info(logMessage);
        }
    }
}
