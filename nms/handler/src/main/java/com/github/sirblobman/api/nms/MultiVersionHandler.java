package com.github.sirblobman.api.nms;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.bossbar.BossBarHandler;
import com.github.sirblobman.api.nms.scoreboard.ScoreboardHandler;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;

public final class MultiVersionHandler {
    private final JavaPlugin plugin;

    private BossBarHandler bossBarHandler;
    private ScoreboardHandler scoreboardHandler;
    private EntityHandler entityHandler;
    private HeadHandler headHandler;
    private ItemHandler itemHandler;
    private PlayerHandler playerHandler;

    public MultiVersionHandler(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    private Class<?> findHandlerClass(String classType) throws ClassNotFoundException {
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();
        String className = ("com.github.sirblobman.api.nms." + classType + "_" + nmsVersion);
        return Class.forName(className);
    }

    private <O> O getHandler(Class<O> typeClass, String classType) {
        JavaPlugin plugin = getPlugin();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        try {
            Class<?> handlerClass = findHandlerClass(classType);
            Class<? extends O> aClass = handlerClass.asSubclass(typeClass);
            Constructor<? extends O> constructor = aClass.getDeclaredConstructor(JavaPlugin.class);
            return constructor.newInstance(plugin);
        } catch (ReflectiveOperationException ex) {
            Logger logger = plugin.getLogger();
            logger.warning("Could not find '" + classType + "' for version '" + nmsVersion
                    + "'. Searching for fallback handler...");
            String className = ("com.github.sirblobman.api.nms." + classType + "_Fallback");

            try {
                Class<?> fallbackClass = Class.forName(className);
                Class<? extends O> aClass = fallbackClass.asSubclass(typeClass);
                Constructor<? extends O> constructor = aClass.getDeclaredConstructor(JavaPlugin.class);
                return constructor.newInstance(plugin);
            } catch (ReflectiveOperationException ex2) {
                logger.log(Level.WARNING, "Original Error that caused fallback search:", ex);
                throw new IllegalStateException("Missing fallback class '" + className + "'.", ex2);
            }
        }
    }

    public BossBarHandler getBossBarHandler() {
        if (this.bossBarHandler == null) {
            JavaPlugin plugin = getPlugin();
            this.bossBarHandler = new BossBarHandler(plugin);
        }

        return this.bossBarHandler;
    }

    public ScoreboardHandler getScoreboardHandler() {
        if (this.scoreboardHandler == null) {
            JavaPlugin plugin = getPlugin();
            this.scoreboardHandler = new ScoreboardHandler(plugin);
        }

        return this.scoreboardHandler;
    }

    public EntityHandler getEntityHandler() {
        if (this.entityHandler != null) {
            return this.entityHandler;
        }

        this.entityHandler = getHandler(EntityHandler.class, "EntityHandler");
        return getEntityHandler();
    }

    public PlayerHandler getPlayerHandler() {
        if (this.playerHandler != null) {
            return this.playerHandler;
        }

        this.playerHandler = getHandler(PlayerHandler.class, "PlayerHandler");
        return getPlayerHandler();
    }

    public HeadHandler getHeadHandler() {
        if (this.headHandler != null) {
            return this.headHandler;
        }

        this.headHandler = getHandler(HeadHandler.class, "HeadHandler");
        return getHeadHandler();
    }

    public ItemHandler getItemHandler() {
        if (this.itemHandler != null) {
            return this.itemHandler;
        }

        this.itemHandler = getHandler(ItemHandler.class, "ItemHandler");
        return getItemHandler();
    }
}
