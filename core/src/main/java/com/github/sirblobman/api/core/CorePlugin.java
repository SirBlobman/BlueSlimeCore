package com.github.sirblobman.api.core;

import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;

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
        if(isDebugMode()) {
            Logger logger = getLogger();
            logger.info("[Debug] Plugin version: " + getDescription().getVersion());
            logger.info("[Debug] Server Version: " + Bukkit.getVersion());
            logger.info("[Debug] Bukkit Version: " + Bukkit.getBukkitVersion());
            logger.info("[Debug] Minecraft Version: " + VersionUtility.getMinecraftVersion());
            logger.info("[Debug] NMS Version: " + VersionUtility.getNetMinecraftServerVersion());
            logger.info("[Debug] Major.Minor Version: " + VersionUtility.getMajorMinorVersion());
            logger.info("[Debug] Major Version: " + VersionUtility.getMajorVersion());
            logger.info("[Debug] Minor Version: " + VersionUtility.getMinorVersion());
        }

        LanguageManager languageManager = getLanguageManager();
        languageManager.reloadLanguageFiles();
        
        registerCommands();
        registerListeners();
        printMultiVersionInformation();
        
        UpdateManager updateManager = getUpdateManager();
        updateManager.addResource(this, 83189L);
        updateManager.checkForUpdates();
    }
    
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
    
    public boolean isDebugMode() {
        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        return configuration.getBoolean("debug-mode", false);
    }
    
    public UpdateManager getUpdateManager() {
        return this.updateManager;
    }
    
    private void printMultiVersionInformation() {
        if(!isDebugMode()) {
            return;
        }
        
        Logger logger = getLogger();
        String minecraftVersion = VersionUtility.getMinecraftVersion();
        logger.info("Minecraft Version: " + minecraftVersion);
        
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();
        logger.info("NMS Version: " + nmsVersion);
        
        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        logger.info("Attempting for find NMS handlers for version '" + nmsVersion + "'...");
        
        BossBarHandler bossBarHandler = multiVersionHandler.getBossBarHandler();
        EntityHandler entityHandler = multiVersionHandler.getEntityHandler();
        HeadHandler headHandler = multiVersionHandler.getHeadHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        PlayerHandler playerHandler = multiVersionHandler.getPlayerHandler();
        ScoreboardHandler scoreboardHandler = multiVersionHandler.getScoreboardHandler();
        
        logger.info("Successfully linked with the following handlers:");
        printClassNames(bossBarHandler, scoreboardHandler, entityHandler, headHandler, itemHandler, playerHandler);
        
        logger.info("Boss Bar Wrapper:");
        printClassNames(bossBarHandler.getWrapperClass());
    }
    
    private void printClassNames(Object... objects) {
        for(Object object : objects) {
            if(object != null) {
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
        if(configuration.getBoolean("command-logger", false)) {
            new ListenerCommandLogger(this).register();
        }
        
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion >= 12) {
            new ListenerLocaleChange(this).register();
        }
    }
}
