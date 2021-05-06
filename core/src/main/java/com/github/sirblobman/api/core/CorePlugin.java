package com.github.sirblobman.api.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.command.CommandDebugEvent;
import com.github.sirblobman.api.core.command.CommandGlobalGamerule;
import com.github.sirblobman.api.core.command.CommandItemInfo;
import com.github.sirblobman.api.core.command.CommandItemToBase64;
import com.github.sirblobman.api.core.command.CommandItemToNBT;
import com.github.sirblobman.api.core.command.CommandItemToYML;
import com.github.sirblobman.api.core.listener.ListenerCommandLogger;
import com.github.sirblobman.api.core.listener.ListenerLanguage;
import com.github.sirblobman.api.core.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.nms.EntityHandler;
import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.nms.PlayerHandler;
import com.github.sirblobman.api.nms.bossbar.BossBarHandler;
import com.github.sirblobman.api.nms.scoreboard.ScoreboardHandler;
import com.github.sirblobman.api.update.UpdateManager;
import com.github.sirblobman.api.utility.VersionUtility;

public final class CorePlugin extends ConfigurablePlugin {
    private final UpdateManager updateManager;
    public CorePlugin() {
        this.updateManager = new UpdateManager(this);
    }

    @Override
    public void onLoad() {
        Logger logger = getLogger();
        logger.info("Loading SirBlobman Core...");

        ConfigurationManager configurationManager = getConfigurationManager();
        configurationManager.saveDefault("config.yml");

        logger.info("Successfully loaded SirBlobman Core.");
    }

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        logger.info("Enabling SirBlobman Core...");
        printMultiVersionInformation();

        UpdateManager updateManager = getUpdateManager();
        updateManager.addResource(this, 83189L);

        new CommandDebugEvent(this).register();
        new CommandGlobalGamerule(this).register();
        new CommandItemInfo(this).register();
        new CommandItemToBase64(this).register();
        new CommandItemToNBT(this).register();
        new CommandItemToYML(this).register();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ListenerLanguage(), this);

        ConfigurationManager configurationManager = getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("config.yml");
        if(configuration.getBoolean("command-logger", false)) {
            pluginManager.registerEvents(new ListenerCommandLogger(this), this);
        }

        updateManager.checkForUpdates();
        logger.info("Successfully enabled SirBlobman Core.");
    }

    @Override
    public void onDisable() {
        Logger logger = getLogger();
        logger.info("Disabling SirBlobman Core...");
        logger.info("Successfully disabled SirBlobman Core.");
    }

    public UpdateManager getUpdateManager() {
        return this.updateManager;
    }

    private void printMultiVersionInformation() {
        Logger logger = getLogger();
        String minecraftVersion = VersionUtility.getMinecraftVersion();
        logger.info("Minecraft Version: " + minecraftVersion);

        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();
        logger.info("NMS Version: "  + nmsVersion);

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
    }

    private void printClassNames(Object... objectArray) {
        Logger logger = getLogger();
        for(Object object : objectArray) {
            Class<?> objectClass = object.getClass();
            String className = objectClass.getName();
            logger.info(" - " + className);
        }
    }
}