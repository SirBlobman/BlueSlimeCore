package com.github.sirblobman.api.language.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.language.LanguageManager;

public final class LanguageListener implements Listener {
    private final Plugin plugin;
    private final LanguageManager languageManager;

    public LanguageListener(@NotNull Plugin plugin, @NotNull LanguageManager languageManager) {
        this.plugin = plugin;
        this.languageManager = languageManager;
    }

    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    public @NotNull LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public void register() {
        Plugin plugin = getPlugin();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, plugin);

        LanguageManager languageManager = getLanguageManager();
        languageManager.printDebug("Registered language listener.");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        runLater(() -> updateLocale(player));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSwitchLocale(PlayerLocaleChangeEvent e) {
        Player player = e.getPlayer();
        runLater(() -> updateLocale(player));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        runLater(() -> remove(player));
    }

    private void runLater(@NotNull Runnable task) {
        Plugin plugin = getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, task, 1L);
    }

    private void updateLocale(@NotNull Player player) {
        String playerLocale = player.getLocale();
        LanguageManager languageManager = getLanguageManager();
        languageManager.setLocale(player, playerLocale);
    }

    private void remove(@NotNull Player player) {
        LanguageManager languageManager = getLanguageManager();
        languageManager.removeLocale(player);
    }
}
