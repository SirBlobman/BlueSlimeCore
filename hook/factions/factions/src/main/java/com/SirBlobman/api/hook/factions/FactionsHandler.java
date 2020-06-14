package com.SirBlobman.api.hook.factions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FactionsHandler<Plugin extends JavaPlugin> {
    private final Plugin plugin;
    private final HookFactions<Plugin, ?> hookFactions;
    public FactionsHandler(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.hookFactions = setupFactionsHook();
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public HookFactions<Plugin, ?> getFactionsHook() {
        return this.hookFactions;
    }
    
    private HookFactions<Plugin, ?> setupFactionsHook() {
        PluginManager manager = Bukkit.getPluginManager();
        if(manager.isPluginEnabled("FactionsX")) {
            printHookInfo("FactionsX", "FactionsX");
            return new HookFactionsX<>(this.plugin);
        }
        
        if(manager.isPluginEnabled("LegacyFactions")) {
            printHookInfo("LegacyFactions", "Legacy Factions");
            return new HookLegacyFactions<>(this.plugin);
        }
        
        if(manager.isPluginEnabled("Factions")) {
            List<String> authorList = getAuthorListForFactions();
            if(authorList.contains("drtshock")) {
                if(authorList.contains("LockedThread")) printHookInfo("Factions", "LockedThread's Faction Fork");
                else if(authorList.contains("ProSavage")) printHookInfo("Factions", "SavageFactions");
                else printHookInfo("Factions", "FactionsUUID");
                
                return new HookFactionsUUID<>(this.plugin);
            }
            
            printHookInfo("Factions", "MassiveCore Factions");
            return new HookMassiveCore<>(this.plugin);
        }
        
        throw new IllegalStateException("Could not find a valid Factions plugin to hook into.");
    }
    
    private org.bukkit.plugin.Plugin getPlugin(String pluginName) {
        PluginManager manager = Bukkit.getPluginManager();
        return manager.getPlugin(pluginName);
    }
    
    private void printHookInfo(String pluginName, String displayName) {
        org.bukkit.plugin.Plugin plugin = getPlugin(pluginName);
        if(plugin == null) return;
    
        PluginDescriptionFile description = plugin.getDescription();
        String version = description.getVersion();
        
        Logger logger = this.plugin.getLogger();
        logger.info("Successfully hooked into '" + displayName + " " + version + " '.");
    }
    
    private List<String> getAuthorListForFactions() {
        org.bukkit.plugin.Plugin plugin = getPlugin("Factions");
        if(plugin == null) return Collections.emptyList();
    
        PluginDescriptionFile description = plugin.getDescription();
        return description.getAuthors();
    }
}