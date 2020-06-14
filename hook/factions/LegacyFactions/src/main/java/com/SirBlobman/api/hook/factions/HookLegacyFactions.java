package com.SirBlobman.api.hook.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.redstoneore.legacyfactions.Factions;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.locality.Locality;

public class HookLegacyFactions<Plugin extends JavaPlugin> extends HookFactions<Plugin, Factions> {
    public HookLegacyFactions(Plugin plugin) {
        super(plugin, Factions.class);
    }
    
    @Override
    public Faction getFactionAt(Location location) {
        Locality locality = Locality.of(location);
        return locality.getFactionHere();
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        FPlayer fplayer = FPlayerColl.get(player);
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