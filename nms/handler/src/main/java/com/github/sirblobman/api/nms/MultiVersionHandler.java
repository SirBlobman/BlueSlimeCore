package com.github.sirblobman.api.nms;

import java.lang.reflect.Constructor;
import java.util.Locale;
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
     * @return A class object for the class found at 'com.github.sirblobman.api.nms.[classType]_[nmsVersion]'
     * @throws ClassNotFoundException when the class does not exist.
     */
    private @NotNull Class<?> findHandlerClass(@NotNull String classType, @NotNull String nmsVersion)
            throws ReflectiveOperationException {
        Class<?> thisClass = getClass();
        Package thisPackage = thisClass.getPackage();
        String packageName = thisPackage.getName();

        JavaPlugin plugin = getPlugin();
        Logger logger = plugin.getLogger();
        String className = String.format(Locale.US, "%s.%s_%s", packageName, classType, nmsVersion);

        try {
            logger.info("Searching for class named '" + className + "'.");
            return Class.forName(className);
        } catch (NoClassDefFoundError ex) {
            logger.log(Level.WARNING, "Failed to find class '" + className + "'.");
            logger.log(Level.WARNING, "Original error:", ex);
            throw new IllegalStateException(ex);
        }
    }

    private <O> @NotNull O convertClass(@NotNull Class<O> typeClass, Class<?> preClass)
            throws ReflectiveOperationException {
        JavaPlugin plugin = getPlugin();
        Class<? extends O> subClass = preClass.asSubclass(typeClass);
        Constructor<? extends O> constructor = subClass.getDeclaredConstructor(JavaPlugin.class);
        return constructor.newInstance(plugin);
    }

    private <O> @NotNull O getHandler(@NotNull Class<O> typeClass, @NotNull String classType) {
        JavaPlugin plugin = getPlugin();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        try {
            Class<?> handlerClass = findHandlerClass(classType, nmsVersion);
            return convertClass(typeClass, handlerClass);
        } catch (ReflectiveOperationException ex) {
            Logger logger = plugin.getLogger();
            logger.warning("Failed to find handler class with type '" + typeClass + "'.");
            logger.warning("Possibly missing support for NMS version '" + nmsVersion + "'.");
            logger.warning("Searching for fallback handler...");

            try {
                Class<?> handlerClass = findHandlerClass(classType, "Fallback");
                return convertClass(typeClass, handlerClass);
            } catch (ReflectiveOperationException ex2) {
                logger.log(Level.WARNING, "Failed to find fallback class with type '" + typeClass + "'.");
                logger.log(Level.WARNING, "Original error that caused fallback search:", ex);
                throw new IllegalStateException(ex2);
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
