package com.github.sirblobman.api.core.listener;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;

public final class ListenerLanguage extends PluginListener<CorePlugin> {
    public ListenerLanguage(CorePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Set<ConfigurablePlugin> pluginSet = getLanguagePlugins();
        for(ConfigurablePlugin plugin : pluginSet) {
            LanguageManager languageManager = plugin.getLanguageManager();
            languageManager.removeCachedLanguage(player);
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
