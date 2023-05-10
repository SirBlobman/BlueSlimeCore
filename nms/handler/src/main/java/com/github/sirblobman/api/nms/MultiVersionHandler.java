package com.github.sirblobman.api.nms;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.bossbar.BossBarHandler;
import com.github.sirblobman.api.nms.scoreboard.ScoreboardHandler;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.utility.paper.PaperChecker;

public final class MultiVersionHandler {
    private final JavaPlugin plugin;

    private BossBarHandler bossBarHandler;
    private ScoreboardHandler scoreboardHandler;
    private EntityHandler entityHandler;
    private HeadHandler headHandler;
    private ItemHandler itemHandler;
    private PlayerHandler playerHandler;
    private ServerHandler serverHandler;

    /**
     * Create a new instance of a handler for multiple versions in a plugin.
     * @param plugin The plugin that owns this instance.
     */
    public MultiVersionHandler(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @return the plugin that owns this instance.
     */
    public @NotNull JavaPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * Find a handler class based on the current NMS version.
     * @param classType The prefix name of the handler class (e.g. ItemHandler)
     * @return A class object for the class found at 'com.github.sirblobman.api.nms.[classType]_[nmsVersion]
     * @throws ClassNotFoundException when the class does not exist.
     */
    private @NotNull Class<?> findHandlerClass(@NotNull String classType) throws ClassNotFoundException {
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();
        String className = ("com.github.sirblobman.api.nms." + classType + "_" + nmsVersion);
        return Class.forName(className);
    }

    private @NotNull<O> O getHandler(@NotNull Class<O> typeClass, @NotNull String classType) {
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

    public @NotNull BossBarHandler getBossBarHandler() {
        if (this.bossBarHandler == null) {
            JavaPlugin plugin = getPlugin();
            this.bossBarHandler = new BossBarHandler(plugin);
        }

        return this.bossBarHandler;
    }

    public @NotNull ScoreboardHandler getScoreboardHandler() {
        if (this.scoreboardHandler == null) {
            JavaPlugin plugin = getPlugin();
            this.scoreboardHandler = new ScoreboardHandler(plugin);
        }

        return this.scoreboardHandler;
    }

    public @NotNull EntityHandler getEntityHandler() {
        if (this.entityHandler != null) {
            return this.entityHandler;
        }

        if (PaperChecker.isPaper() && PaperChecker.hasNativeComponentSupport()) {
            JavaPlugin plugin = getPlugin();
            this.entityHandler = new EntityHandler_Paper(plugin);
            return this.entityHandler;
        }

        this.entityHandler = getHandler(EntityHandler.class, "EntityHandler");
        return this.entityHandler;
    }

    public @NotNull PlayerHandler getPlayerHandler() {
        if (this.playerHandler != null) {
            return this.playerHandler;
        }

        if (PaperChecker.isPaper() && PaperChecker.hasNativeComponentSupport()) {
            JavaPlugin plugin = getPlugin();
            this.playerHandler = new PlayerHandler_Paper(plugin);
            return this.playerHandler;
        }

        this.playerHandler = getHandler(PlayerHandler.class, "PlayerHandler");
        return this.playerHandler;
    }

    public @NotNull HeadHandler getHeadHandler() {
        if (this.headHandler != null) {
            return this.headHandler;
        }

        if (PaperChecker.isPaper() && PaperChecker.hasNativeComponentSupport()) {
            JavaPlugin plugin = getPlugin();
            this.headHandler = new HeadHandler_Paper(plugin);
            return this.headHandler;
        }

        this.headHandler = getHandler(HeadHandler.class, "HeadHandler");
        return this.headHandler;
    }

    public @NotNull ItemHandler getItemHandler() {
        if (this.itemHandler != null) {
            return this.itemHandler;
        }

        ItemHandler nmsItemHandler = getHandler(ItemHandler.class, "ItemHandler");
        if (PaperChecker.isPaper() && PaperChecker.hasNativeComponentSupport()) {
            JavaPlugin plugin = getPlugin();
            this.itemHandler = new ItemHandler_Paper(plugin, nmsItemHandler);
            return this.itemHandler;
        }

        return (this.itemHandler = nmsItemHandler);
    }

    public @NotNull ServerHandler getServerHandler() {
        if (this.serverHandler != null) {
            return this.serverHandler;
        }

        if (PaperChecker.isPaper() && PaperChecker.hasNativeComponentSupport()) {
            JavaPlugin plugin = getPlugin();
            this.serverHandler = new ServerHandler_Paper(plugin);
            return this.serverHandler;
        }

        this.serverHandler = getHandler(ServerHandler.class, "ServerHandler");
        return this.serverHandler;
    }
}
