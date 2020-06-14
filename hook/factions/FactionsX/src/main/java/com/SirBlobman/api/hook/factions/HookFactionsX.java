package com.SirBlobman.api.hook.factions;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.prosavage.factionsx.FactionsX;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;

public class HookFactionsX<Plugin extends JavaPlugin> extends HookFactions<Plugin, FactionsX> {
    public HookFactionsX(Plugin plugin) {
        super(plugin, FactionsX.class);
    }
    
    @Override
    public Faction getFactionAt(Location location) {
        Chunk chunk = location.getChunk();
        return GridManager.INSTANCE.getFactionAt(chunk);
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(player);
        return fplayer.getFaction();
    }
    
    @Override
    public boolean isSafeZone(Location location) {
        Faction faction = getFactionAt(location);
        return faction.isSafezone();
    }
    
    @Override
    public boolean isWarZone(Location location) {
        Faction faction = getFactionAt(location);
        return faction.isWarzone();
    }
    
    @Override
    public boolean isWilderness(Location location) {
        Faction faction = getFactionAt(location);
        return faction.isWilderness();
    }
}