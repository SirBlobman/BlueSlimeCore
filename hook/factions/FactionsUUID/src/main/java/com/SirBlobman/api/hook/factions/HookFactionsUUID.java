package com.SirBlobman.api.hook.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.*;

public class HookFactionsUUID<Plugin extends JavaPlugin> extends HookFactions<Plugin, FactionsPlugin> {
    public HookFactionsUUID(Plugin plugin) {
        super(plugin, FactionsPlugin.class);
    }
    
    @Override
    public Faction getFactionAt(Location location) {
        Board board = Board.getInstance();
        FLocation flocation = new FLocation(location);
        return board.getFactionAt(flocation);
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
        return fplayer.getFaction();
    }
    
    @Override
    public boolean isSafeZone(Location location) {
        Faction faction = getFactionAt(location);
        return faction.isSafeZone();
    }
    
    @Override
    public boolean isWarZone(Location location) {
        Faction faction = getFactionAt(location);
        return faction.isWarZone();
    }
    
    @Override
    public boolean isWilderness(Location location) {
        Faction faction = getFactionAt(location);
        return faction.isWilderness();
    }
}