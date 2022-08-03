package com.github.sirblobman.api.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.LanguageCache;
import com.github.sirblobman.api.plugin.listener.PluginListener;

public final class ListenerLocaleChange extends PluginListener<CorePlugin> {
    public ListenerLocaleChange(CorePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChange(PlayerLocaleChangeEvent e) {
        if (shouldUpdate()) {
            Player player = e.getPlayer();
            updateLater(player);
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

    private boolean shouldUpdate() {
        YamlConfiguration configuration = getConfiguration();
        return configuration.getBoolean("cache-language-update-on-change");
    }
}
