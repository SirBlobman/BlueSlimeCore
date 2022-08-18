package com.github.sirblobman.api.bossbar;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Flag;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

public final class BossBarHandler {
    private final JavaPlugin plugin;
    private final BukkitAudiences audiences;
    private final Map<Key, BossBar> bossBarMap;
    private final Map<UUID, Audience> audienceMap;
    private final Component defaultTitle;

    public BossBarHandler(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.audiences = BukkitAudiences.create(plugin);
        this.bossBarMap = new ConcurrentHashMap<>();
        this.audienceMap = new ConcurrentHashMap<>();
        this.defaultTitle = Component.text("Default Title");
    }

    @NotNull
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    public Component getDefaultTitle() {
        return this.defaultTitle;
    }

    @NotNull
    public Key getKey(@Subst("key") String key) {
        Validate.notNull(key, "key must not be null!");

        JavaPlugin plugin = getPlugin();
        @Subst("plugin") String pluginName = plugin.getName().toLowerCase(Locale.US);
        return Key.key(pluginName, key);
    }

    @NotNull
    public Audience getAudience(Player player) {
        Validate.notNull(player, "player must not be null!");
        UUID playerId = player.getUniqueId();
        return this.audienceMap.computeIfAbsent(playerId, key -> this.audiences.player(player));
    }

    @NotNull
    public BossBar getBossBar(String key) {
        Key adventureKey = getKey(key);
        BossBar bossBar = this.bossBarMap.get(adventureKey);
        if(bossBar != null) {
            return bossBar;
        }

        Component defaultTitle = getDefaultTitle();
        BossBar newBossBar = BossBar.bossBar(defaultTitle, 1.0F, Color.PURPLE, Overlay.PROGRESS);
        this.bossBarMap.put(adventureKey, newBossBar);
        return newBossBar;
    }

    public void removeBossBar(String key) {
        Key adventureKey = getKey(key);
        BossBar bossBar = this.bossBarMap.get(adventureKey);
        if(bossBar == null) {
            return;
        }

        Collection<Audience> audienceCollection = this.audienceMap.values();
        for (Audience audience : audienceCollection) {
            audience.hideBossBar(bossBar);
        }

        this.bossBarMap.remove(adventureKey);
    }

    public void showBossBar(Player player, String key) {
        BossBar bossBar = getBossBar(key);
        Audience audience = getAudience(player);
        audience.showBossBar(bossBar);
    }

    public void updateBossBar(String key, Component title, float progress, Color color, Overlay style) {
        BossBar bossBar = getBossBar(key);
        bossBar.name(title);
        bossBar.progress(progress);
        bossBar.color(color);
        bossBar.overlay(style);
    }

    public void addFlag(String key, Flag... flags) {
        BossBar bossBar = getBossBar(key);
        bossBar.addFlags(flags);
    }

    public void removeFlag(String key, Flag... flags) {
        BossBar bossBar = getBossBar(key);
        bossBar.removeFlags(flags);
    }
}
