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
import com.github.sirblobman.api.folia.IFoliaPlugin;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.language.LanguageManager;

public final class LanguageListener<P extends Plugin> implements Listener {
    private final IFoliaPlugin<P> plugin;
    private final LanguageManager languageManager;

    public LanguageListener(@NotNull IFoliaPlugin<P> plugin, @NotNull LanguageManager languageManager) {
        this.plugin = plugin;
        this.languageManager = languageManager;
    }

    private @NotNull IFoliaPlugin<P> getFoliaPlugin() {
        return this.plugin;
    }

    private @NotNull P getPlugin() {
        IFoliaPlugin<P> plugin = getFoliaPlugin();
        return plugin.getPlugin();
    }

    private @NotNull FoliaHelper<P> getFoliaHelper() {
        IFoliaPlugin<P> plugin = getFoliaPlugin();
        return plugin.getFoliaHelper();
    }

    private @NotNull TaskScheduler<P> getTaskScheduler() {
        FoliaHelper<P> foliaHelper = getFoliaHelper();
        return foliaHelper.getScheduler();
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
        P plugin = getPlugin();
        Player player = e.getPlayer();
        LanguageManager languageManager = getLanguageManager();

        UpdateLocaleTask<P> task = new UpdateLocaleTask<>(plugin, player, languageManager);
        task.setDelay(1L);

        TaskScheduler<P> taskScheduler = getTaskScheduler();
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
