package com.github.sirblobman.api.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.core.configuration.CoreConfiguration;
import com.github.sirblobman.api.language.LanguageCache;
import com.github.sirblobman.api.plugin.listener.PluginListener;

public final class ListenerLanguage extends PluginListener<CorePlugin> {
    public ListenerLanguage(CorePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        if (isCacheOnJoin()) {
            Player player = e.getPlayer();
            updateLater(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        if (isRemoveOnQuit()) {
            Player player = e.getPlayer();
            LanguageCache.removeCachedLocale(player);
        }
    }

    private void updateLater(Player player) {
        CorePlugin corePlugin = getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Runnable task = () -> LanguageCache.updateCachedLocale(player);
        scheduler.scheduleSyncDelayedTask(corePlugin, task);
    }

    private CoreConfiguration getConfiguration() {
        CorePlugin plugin = getPlugin();
        return plugin.getCoreConfiguration();
    }

    private boolean isCacheOnJoin() {
        CoreConfiguration configuration = getConfiguration();
        return configuration.isCacheLanguageOnJoin();
    }

    private boolean isRemoveOnQuit() {
        CoreConfiguration configuration = getConfiguration();
        return configuration.isRemoveCacheLanguageOnQuit();
    }
}
