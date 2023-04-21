package com.github.sirblobman.api.bossbar;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.shaded.adventure.audience.Audience;
import com.github.sirblobman.api.shaded.adventure.bossbar.BossBar;
import com.github.sirblobman.api.shaded.adventure.bossbar.BossBar.Color;
import com.github.sirblobman.api.shaded.adventure.bossbar.BossBar.Flag;
import com.github.sirblobman.api.shaded.adventure.bossbar.BossBar.Overlay;
import com.github.sirblobman.api.shaded.adventure.key.Key;
import com.github.sirblobman.api.shaded.adventure.platform.bukkit.BukkitAudiences;
import com.github.sirblobman.api.shaded.adventure.text.Component;

import org.intellij.lang.annotations.Subst;

public final class BossBarHandler {
    private final JavaPlugin plugin;
    private final BukkitAudiences audiences;
    private final Map<Key, BossBar> bossBarMap;
    private final Map<UUID, Audience> audienceMap;
    private final Component defaultTitle;

    public BossBarHandler(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.audiences = BukkitAudiences.create(plugin);
        this.bossBarMap = new ConcurrentHashMap<>();
        this.audienceMap = new ConcurrentHashMap<>();
        this.defaultTitle = Component.text("Default Title");
    }

    public @NotNull JavaPlugin getPlugin() {
        return this.plugin;
    }

    public @NotNull Component getDefaultTitle() {
        return this.defaultTitle;
    }

    public @NotNull Key getKey(@NotNull @Subst("key") String key) {
        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
        @Subst("plugin") String pluginNameKey = pluginName.toLowerCase(Locale.US);
        return Key.key(pluginNameKey, key);
    }

    public @NotNull Audience getAudience(@NotNull Player player) {
        Validate.notNull(player, "player must not be null!");
        UUID playerId = player.getUniqueId();
        return this.audienceMap.computeIfAbsent(playerId, key -> this.audiences.player(player));
    }

    public @NotNull BossBar getBossBar(@NotNull String key) {
        Key adventureKey = getKey(key);
        BossBar bossBar = this.bossBarMap.get(adventureKey);
        if (bossBar != null) {
            return bossBar;
        }

        Component defaultTitle = getDefaultTitle();
        BossBar newBossBar = BossBar.bossBar(defaultTitle, 1.0F, Color.PURPLE, Overlay.PROGRESS);
        this.bossBarMap.put(adventureKey, newBossBar);
        return newBossBar;
    }

    public void removeBossBar(@NotNull String key) {
        Key adventureKey = getKey(key);
        BossBar bossBar = this.bossBarMap.get(adventureKey);
        if (bossBar == null) {
            return;
        }

        Collection<Audience> audienceCollection = this.audienceMap.values();
        for (Audience audience : audienceCollection) {
            audience.hideBossBar(bossBar);
        }

        this.bossBarMap.remove(adventureKey);
    }

    public void showBossBar(@NotNull Player player, @NotNull String key) {
        BossBar bossBar = getBossBar(key);
        Audience audience = getAudience(player);
        audience.showBossBar(bossBar);
    }

    public void updateBossBar(@NotNull String key, @NotNull Component title, float progress,
                              @NotNull Color color, @NotNull Overlay style) {
        BossBar bossBar = getBossBar(key);
        bossBar.name(title);
        bossBar.progress(progress);
        bossBar.color(color);
        bossBar.overlay(style);
    }

    public void addFlag(@NotNull String key, Flag @NotNull... flags) {
        BossBar bossBar = getBossBar(key);
        bossBar.addFlags(flags);
    }

    public void removeFlag(@NotNull String key, Flag @NotNull... flags) {
        BossBar bossBar = getBossBar(key);
        bossBar.removeFlags(flags);
    }
}
