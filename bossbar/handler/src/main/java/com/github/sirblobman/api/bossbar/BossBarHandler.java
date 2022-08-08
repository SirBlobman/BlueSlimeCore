package com.github.sirblobman.api.bossbar;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.bossbar.modern.BossBarWrapper_Modern;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.bossbar.legacy.BossBarWrapper_Legacy;
import com.github.sirblobman.api.bossbar.modern.BossBarWrapper_Paper;

import org.jetbrains.annotations.Nullable;

public final class BossBarHandler {
    private final JavaPlugin plugin;
    private final Class<? extends BossBarWrapper> wrapperClass;
    private final Map<Player, BossBarWrapper> bossBarMap;

    public BossBarHandler(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.wrapperClass = setupWrapperClass();
        this.bossBarMap = new WeakHashMap<>();
    }

    @Nullable
    public Class<? extends BossBarWrapper> getWrapperClass() {
        return this.wrapperClass;
    }

    @Nullable
    public BossBarWrapper getBossBar(Player player) {
        BossBarWrapper wrapper = this.bossBarMap.getOrDefault(player, null);
        if (wrapper != null) return wrapper;

        try {
            Constructor<? extends BossBarWrapper> constructor = this.wrapperClass.getConstructor(Player.class);
            BossBarWrapper bossBarWrapper = constructor.newInstance(player);
            this.bossBarMap.put(player, bossBarWrapper);
            return bossBarWrapper;
        } catch (ReflectiveOperationException ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "Failed to get a boss bar for a player because an error occurred:", ex);
            return null;
        }
    }

    public void updateBossBar(Player player, String message, double progress, String color, String style) {
        BossBarWrapper wrapper = getBossBar(player);
        if (wrapper == null) return;

        wrapper.setTitle(message);
        wrapper.setProgress(progress);
        wrapper.setColor(color);
        wrapper.setStyle(style);

        wrapper.addExtraPlayer(player);
        wrapper.setVisible(true);
    }

    public void removeBossBar(Player player) {
        BossBarWrapper wrapper = getBossBar(player);
        if (wrapper == null) return;

        wrapper.setVisible(false);
        wrapper.removeExtraPlayer(player);
    }

    private Class<? extends BossBarWrapper> setupWrapperClass() {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 9) {
            return BossBarWrapper_Legacy.class;
        }

        if (minorVersion < 16) {
            return BossBarWrapper_Modern.class;
        }

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            Class.forName("net.kyori.adventure.bossbar.BossBar");
            return BossBarWrapper_Paper.class;
        } catch (Exception ex) {
            return BossBarWrapper_Modern.class;
        }
    }
}
