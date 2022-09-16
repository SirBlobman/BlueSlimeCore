package com.github.sirblobman.api.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.core.configuration.CoreConfiguration;
import com.github.sirblobman.api.language.LanguageCache;
import com.github.sirblobman.api.plugin.listener.PluginListener;

public final class ListenerLocaleChange extends PluginListener<CorePlugin> {
    public ListenerLocaleChange(CorePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChange(PlayerLocaleChangeEvent e) {
        if (isUpdateOnChange()) {
            Player player = e.getPlayer();
            updateLater(player);
        }
    }

    private void updateLater(Player player) {
        CorePlugin corePlugin = getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Runnable task = () -> LanguageCache.updateCachedLocale(player);
        scheduler.runTaskLater(corePlugin, task, 2L);
    }

    private CoreConfiguration getConfiguration() {
        CorePlugin plugin = getPlugin();
        return plugin.getCoreConfiguration();
    }

    private boolean isUpdateOnChange() {
        CoreConfiguration configuration = getConfiguration();
        return configuration.isUpdateCacheLanguageOnChange();
    }
}
