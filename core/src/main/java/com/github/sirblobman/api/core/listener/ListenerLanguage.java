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
import com.github.sirblobman.api.language.LanguageManager;

public final class ListenerLanguage extends PluginListener<CorePlugin> {
    public ListenerLanguage(CorePlugin plugin) {
        super(plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        if(shouldCacheOnJoin()) {
            Player player = e.getPlayer();
            CorePlugin plugin = getPlugin();
            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> LanguageManager.updateCachedLocale(player), 1L);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        if(shouldRemoveOnQuit()) {
            Player player = e.getPlayer();
            LanguageManager.removeCachedLocale(player);
        }
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
