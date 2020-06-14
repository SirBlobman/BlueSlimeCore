package com.SirBlobman.api.hook.factions;

import com.SirBlobman.api.handler.Hook;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class HookFactions<Plugin extends JavaPlugin, HookPlugin extends JavaPlugin> extends Hook<Plugin, HookPlugin> {
    public HookFactions(Plugin plugin, Class<HookPlugin> hookPluginClass) {
        super(plugin, hookPluginClass);
    }
    
    public Object getFactionAt(Entity entity) {
        Location location = entity.getLocation();
        return getFactionAt(location);
    }
    
    public abstract Object getFactionAt(Location location);
    public abstract Object getFactionFor(Player player);
    
    public abstract boolean isSafeZone(Location location);
    public abstract boolean isWarZone(Location location);
    public abstract boolean isWilderness(Location location);
}