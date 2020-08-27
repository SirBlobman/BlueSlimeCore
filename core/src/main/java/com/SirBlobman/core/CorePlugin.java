package com.SirBlobman.core;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.configuration.ConfigurationManager;
import com.SirBlobman.api.configuration.PlayerDataManager;
import com.SirBlobman.api.language.LanguageManager;
import com.SirBlobman.api.nms.*;
import com.SirBlobman.api.nms.bossbar.BossBarHandler;
import com.SirBlobman.api.nms.scoreboard.ScoreboardHandler;
import com.SirBlobman.api.utility.VersionUtility;

public class CorePlugin extends JavaPlugin {
    private final MultiVersionHandler multiVersionHandler;
    private final ConfigurationManager configurationManager;
    private final LanguageManager languageManager;
    private final PlayerDataManager playerDataManager;
    public CorePlugin() {
        this.multiVersionHandler = new MultiVersionHandler(this);
        this.configurationManager = new ConfigurationManager(this);
        this.languageManager = new LanguageManager(this, this.configurationManager);
        this.playerDataManager = new PlayerDataManager(this);
    }

    @Override
    public void onLoad() {
        Logger logger = getLogger();
        logger.info("Loading SirBlobman Core...");
        logger.info("Successfully loaded SirBlobman Core.");
    }

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        logger.info("Enabling SirBlobman Core...");
        printMultiVersionInformation();
        logger.info("Successfully enabled SirBlobman Core.");
    }

    @Override
    public void onDisable() {
        Logger logger = getLogger();
        logger.info("Disabling SirBlobman Core...");
        logger.info("Successfully disabled SirBlobman Core.");
    }

    public MultiVersionHandler getMultiVersionHandler() {
        return this.multiVersionHandler;
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
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