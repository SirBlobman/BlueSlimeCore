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

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.FoliaPlugin;
import com.github.sirblobman.api.folia.scheduler.BukkitTaskScheduler;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.language.LanguageManager;

public final class LanguageListener implements Listener {
    private final Plugin plugin;
    private final LanguageManager languageManager;
    private final TaskScheduler scheduler;

    public LanguageListener(@NotNull Plugin plugin, @NotNull LanguageManager languageManager) {
        this.plugin = plugin;

        if (plugin instanceof FoliaPlugin) {
            FoliaHelper foliaHelper = ((FoliaPlugin) plugin).getFoliaHelper();
            this.scheduler = foliaHelper.getScheduler();
        } else {
            this.scheduler = new BukkitTaskScheduler(plugin);
        }

        this.languageManager = languageManager;
    }

    private @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    private @NotNull TaskScheduler getTaskScheduler() {
        return this.scheduler;
    }

    private @NotNull LanguageManager getLanguageManager() {
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
        Plugin plugin = getPlugin();
        Player player = e.getPlayer();
        LanguageManager languageManager = getLanguageManager();

        UpdateLocaleTask task = new UpdateLocaleTask(plugin, player, languageManager);
        task.setDelay(1L);

        TaskScheduler taskScheduler = getTaskScheduler();
        taskScheduler.scheduleEntityTask(task);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSwitchLocale(PlayerLocaleChangeEvent e) {
        Player player = e.getPlayer();
        String locale = e.getLocale();

        LanguageManager languageManager = getLanguageManager();
        if (locale == null) {
            languageManager.removeLocale(player);
        } else {
            languageManager.setLocale(player, locale);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        LanguageManager languageManager = getLanguageManager();
        languageManager.removeLocale(player);
    }
}
