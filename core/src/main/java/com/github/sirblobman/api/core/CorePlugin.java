package com.github.sirblobman.api.core;

import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.command.CommandDebugEvent;
import com.github.sirblobman.api.core.command.CommandGlobalGamerule;
import com.github.sirblobman.api.core.command.CommandItemInfo;
import com.github.sirblobman.api.core.command.CommandItemToBase64;
import com.github.sirblobman.api.core.command.CommandItemToNBT;
import com.github.sirblobman.api.core.command.CommandItemToYML;
import com.github.sirblobman.api.core.listener.ListenerCommandLogger;
import com.github.sirblobman.api.core.listener.ListenerLanguage;
import com.github.sirblobman.api.core.listener.ListenerLocaleChange;
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
import com.github.sirblobman.bossbar.BossBarHandler;

public final class CorePlugin extends ConfigurablePlugin {
    private final UpdateManager updateManager;

    public CorePlugin() {
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

        registerCommands();
        registerListeners();

        if (isDebugMode()) {
            printMultiVersionInformation();
        }

        UpdateManager updateManager = getUpdateManager();
        updateManager.addResource(this, 83189L);
        updateManager.checkForUpdates();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    protected void reloadConfiguration() {
        super.reloadConfiguration();

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguageFiles();
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

        printDebug("Boss Bar Wrapper:");
        printClassNames(bossBarHandler.getWrapperClass());
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
    }

    private void registerListeners() {
        new ListenerLanguage(this).register();

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        if (configuration.getBoolean("command-logger", false)) {
            new ListenerCommandLogger(this).register();
        }

        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion >= 12) {
            new ListenerLocaleChange(this).register();
        }
    }
}
