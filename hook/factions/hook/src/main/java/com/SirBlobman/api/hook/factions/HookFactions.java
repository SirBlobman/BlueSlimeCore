package com.SirBlobman.api.hook.factions;

import com.SirBlobman.api.handler.Hook;

import org.bukkit.ChatColor;
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
    public abstract String getFactionNameAt(Location location);
    public abstract Object getFactionFor(Player player);
    public abstract String getFactionNameFor(Player player);
    public abstract boolean hasFaction(Player player);
    
    public abstract boolean isSafeZone(Location location);
    public abstract boolean isWarZone(Location location);
    public abstract boolean isWilderness(Location location);
    
    /**
     * @param player1 The first player
     * @param player2 The second player
     * @return {@code true} if player2 is the same as player1. {@code false} if either player is not in a faction. {@code true} if player2 has an ally relationship with player1. {@code true} if player2 is in the same faction as player1. {@code false} otherwise
     */
    public abstract boolean isMemberOrAlly(Player player1, Player player2);
    
    /**
     * @param player1 The first player
     * @param player2 The second player
     * @return {@code false} if player2 is the same as player1, {@code false} if either player is not in a faction. {@code true} if player2 has an enemy relationship with player1. {@code false} otherwise
     */
    public abstract boolean isEnemy(Player player1, Player player2);
    
    /**
     * @param player The player to check
     * @return {@code true} if the player has admin bypass mode enabled, {@code false} otherwise.
      */
    public abstract boolean hasBypass(Player player);
    public abstract boolean isEnemyLand(Player player, Location location);
    public abstract boolean isInOwnFaction(Player player, Location location);
    public abstract boolean isLeader(Player player, Location location);
    public abstract boolean canBuild(Player player, Location location);
    
    public abstract ChatColor getRelationChatColor(Player viewer, Player player);
    public abstract String getRolePrefix(Player player);
}