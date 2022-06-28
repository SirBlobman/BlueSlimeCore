package com.github.sirblobman.api.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Language;
import com.github.sirblobman.api.language.LanguageCache;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.listener.PluginListener;

public final class ListenerLanguage extends PluginListener<CorePlugin> {
    public ListenerLanguage(CorePlugin plugin) {
        super(plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        if(shouldCacheOnJoin()) {
            Player player = e.getPlayer();
            updateLater(player);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        if(shouldRemoveOnQuit()) {
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
    
    private YamlConfiguration getConfiguration() {
        CorePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        return configurationManager.get("config.yml");
    }
    
    private boolean shouldCacheOnJoin() {
        YamlConfiguration configuration = getConfiguration();
        return configuration.getBoolean("cache-language-on-join");
    }
    
    private boolean shouldRemoveOnQuit() {
        YamlConfiguration configuration = getConfiguration();
        return configuration.getBoolean("cache-language-remove-on-quit");
    }
}
