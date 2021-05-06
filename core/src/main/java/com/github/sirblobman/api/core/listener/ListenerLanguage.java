package com.github.sirblobman.api.core.listener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.core.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.language.LanguageManager;

public final class ListenerLanguage implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Set<ConfigurablePlugin> pluginSet = getLanguagePlugins();
        for(ConfigurablePlugin plugin : pluginSet) {
            LanguageManager languageManager = plugin.getLanguageManager();
            languageManager.updateCachedLocale(player);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Set<ConfigurablePlugin> pluginSet = getLanguagePlugins();
        for(ConfigurablePlugin plugin : pluginSet) {
            LanguageManager languageManager = plugin.getLanguageManager();
            languageManager.removeCachedLocale(player);
        }
    }

    private Set<ConfigurablePlugin> getLanguagePlugins() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin[] pluginArray = pluginManager.getPlugins();

        Set<ConfigurablePlugin> pluginSet = new HashSet<>();
        for(Plugin plugin : pluginArray) {
            if(plugin instanceof ConfigurablePlugin) {
                pluginSet.add((ConfigurablePlugin) plugin);
            }
        }

        return pluginSet;
    }
}
