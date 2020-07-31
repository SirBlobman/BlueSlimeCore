package com.SirBlobman.api.handler;

import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Hook<Plugin extends JavaPlugin, HookPlugin extends JavaPlugin> {
    private final Plugin plugin;
    private final Class<HookPlugin> hookPluginClass;
    public Hook(Plugin plugin, Class<HookPlugin> hookPluginClass) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.hookPluginClass = Objects.requireNonNull(hookPluginClass, "hookPluginClass must not be null!");
        printHookInfo();
    }
    
    private void printHookInfo() {
        HookPlugin hookPlugin = getHookPlugin();
        PluginDescriptionFile hookPluginDescription = hookPlugin.getDescription();
        String hookPluginFullName = hookPluginDescription.getFullName();
        
        Logger logger = getLogger();
        logger.info("Successfully hooked into '" + hookPluginFullName + "'.");
    }
    
    public final Plugin getJavaPlugin() {
        return this.plugin;
    }
    
    public Logger getLogger() {
        Plugin plugin = getJavaPlugin();
        return plugin.getLogger();
    }
    
    public final HookPlugin getHookPlugin() {
        return JavaPlugin.getPlugin(this.hookPluginClass);
    }
    
    public final boolean isHookEnabled() {
        HookPlugin hookPlugin = getHookPlugin();
        return hookPlugin.isEnabled();
    }
}