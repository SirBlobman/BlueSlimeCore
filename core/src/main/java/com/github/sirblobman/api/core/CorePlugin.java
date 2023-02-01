package com.github.sirblobman.api.core;

import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;

import com.github.sirblobman.api.bossbar.BossBarHandler;
import com.github.sirblobman.api.bstats.bukkit.Metrics;
import com.github.sirblobman.api.bstats.charts.SimplePie;
import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.command.CommandDebugEvent;
import com.github.sirblobman.api.core.command.CommandGlobalGamerule;
import com.github.sirblobman.api.core.command.CommandItemInfo;
import com.github.sirblobman.api.core.command.CommandItemToBase64;
import com.github.sirblobman.api.core.command.CommandItemToNBT;
import com.github.sirblobman.api.core.command.CommandItemToYML;
import com.github.sirblobman.api.core.command.blueslimecore.CommandBlueSlimeCore;
import com.github.sirblobman.api.core.configuration.CoreConfiguration;
import com.github.sirblobman.api.core.listener.ListenerCommandLogger;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.EntityHandler;
import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.nms.PlayerHandler;
import com.github.sirblobman.api.nms.scoreboard.ScoreboardHandler;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.update.UpdateManager;
import com.github.sirblobman.api.utility.VersionUtility;

public final class CorePlugin extends ConfigurablePlugin {
    private final CoreConfiguration coreConfiguration;

    private final UpdateManager updateManager;

    public CorePlugin() {
        this.coreConfiguration = new CoreConfiguration();
        this.updateManager = new UpdateManager(this);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();

        LanguageManager languageManager = getLanguageManager();
        languageManager.saveDefaultLanguageFiles();
    }

    @Override
    public void onEnable() {
        reloadConfiguration();

        LanguageManager languageManager = getLanguageManager();
        languageManager.onPluginEnable();

        CoreConfiguration coreConfiguration = getCoreConfiguration();
        if (coreConfiguration.isDebugModeEnabled()) {
            printMultiVersionInformation();
        }

        registerCommands();
        registerListeners();
        registerUpdateChecker();
        register_bStats();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    protected void reloadConfiguration() {
        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.reload("config.yml");

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguages();

        YamlConfiguration configuration = configurationManager.get("config.yml");
        CoreConfiguration coreConfiguration = getCoreConfiguration();
        coreConfiguration.load(configuration);

        HandlerList.unregisterAll(this);
        registerListeners();
    }

    public CoreConfiguration getCoreConfiguration() {
        return this.coreConfiguration;
    }

    public UpdateManager getUpdateManager() {
        return this.updateManager;
    }

    private void printMultiVersionInformation() {
        PluginDescriptionFile description = getDescription();
        String pluginVersion = description.getVersion();
        printDebug("Plugin Version: " + pluginVersion);

        Server server = getServer();
        String serverVersion = server.getVersion();
        String bukkitVersion = server.getBukkitVersion();
        printDebug("Server Version: " + serverVersion);
        printDebug("Bukkit Version: " + bukkitVersion);

        String minecraftVersion = VersionUtility.getMinecraftVersion();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();
        String majorMinorVersion = VersionUtility.getMajorMinorVersion();
        int majorVersion = VersionUtility.getMajorVersion();
        int minorVersion = VersionUtility.getMinorVersion();
        printDebug("Minecraft Version: " + minecraftVersion);
        printDebug("Major.Minor: " + majorMinorVersion);
        printDebug("Major: " + majorVersion);
        printDebug("Minor: " + minorVersion);
        printDebug("Detected NMS Version: " + nmsVersion);

        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        printDebug("Attempting for find NMS handlers for version '" + nmsVersion + "'...");

        BossBarHandler bossBarHandler = multiVersionHandler.getBossBarHandler();
        EntityHandler entityHandler = multiVersionHandler.getEntityHandler();
        HeadHandler headHandler = multiVersionHandler.getHeadHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        PlayerHandler playerHandler = multiVersionHandler.getPlayerHandler();
        ScoreboardHandler scoreboardHandler = multiVersionHandler.getScoreboardHandler();

        printDebug("Successfully linked with the following handlers:");
        printClassNames(bossBarHandler, scoreboardHandler, entityHandler, headHandler, itemHandler, playerHandler);
    }

    private void printClassNames(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                printClassName(object);
            }
        }
    }

    private void printClassName(Object object) {
        Logger logger = getLogger();
        String className = getClassName(object);

        String message = String.format(Locale.US, " - %s", className);
        logger.info(message);
    }

    private String getClassName(Object object) {
        Class<?> objectClass = (object instanceof Class ? (Class<?>) object : object.getClass());
        return objectClass.getName();
    }

    private void registerCommands() {
        new CommandDebugEvent(this).register();
        new CommandGlobalGamerule(this).register();
        new CommandItemInfo(this).register();
        new CommandItemToBase64(this).register();
        new CommandItemToNBT(this).register();
        new CommandItemToYML(this).register();
        new CommandBlueSlimeCore(this).register();
    }

    private void registerListeners() {
        CoreConfiguration coreConfiguration = getCoreConfiguration();
        if (coreConfiguration.isCommandLoggerEnabled()) {
            new ListenerCommandLogger(this).register();
        }
    }

    private void registerUpdateChecker() {
        UpdateManager updateManager = getUpdateManager();
        updateManager.addResource(this, 83189L);
        updateManager.checkForUpdates();
    }

    private void register_bStats() {
        Metrics metrics = new Metrics(this, 16089);
        metrics.addCustomChart(new SimplePie("selected_language", this::getDefaultLanguageName));
    }

    private String getDefaultLanguageName() {
        LanguageManager languageManager = getLanguageManager();
        Language defaultLanguage = languageManager.getDefaultLanguage();
        if (defaultLanguage == null) {
            return "none";
        }

        return defaultLanguage.getLanguageName();
    }
}
