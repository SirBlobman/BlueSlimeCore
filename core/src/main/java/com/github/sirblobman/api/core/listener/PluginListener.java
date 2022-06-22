package com.github.sirblobman.api.core.listener;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

/**
 * @deprecated (will be removed soon, deprecated since v2.3.7)
 * Use {@link com.github.sirblobman.api.plugin.listener.PluginListener} instead.
 * @param <Plugin> The plugin class this listener will belong to.
 */
@Deprecated
public abstract class PluginListener<Plugin extends JavaPlugin> implements Listener {
    private final Plugin plugin;
    
    public PluginListener(Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }
    
    public final void register() {
        unregister();
        
        Plugin plugin = getPlugin();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, plugin);
    }
    
    public final void unregister() {
        HandlerList.unregisterAll(this);
    }
    
    protected final Plugin getPlugin() {
        return this.plugin;
    }
    
    protected final Logger getLogger() {
        Plugin plugin = getPlugin();
        return plugin.getLogger();
    }
}
