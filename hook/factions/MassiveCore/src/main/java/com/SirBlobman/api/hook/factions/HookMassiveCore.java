package com.SirBlobman.api.hook.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

public class HookMassiveCore<Plugin extends JavaPlugin> extends HookFactions<Plugin, Factions> {
    public HookMassiveCore(Plugin plugin) {
        super(plugin, Factions.class);
    }
    
    @Override
    public Faction getFactionAt(Location location) {
        PS ps = PS.valueOf(location);
        BoardColl boardColl = BoardColl.get();
        return boardColl.getFactionAt(ps);
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        MPlayer mplayer = MPlayer.get(player);
        return mplayer.getFaction();
    }
    
    @Override
    public boolean isSafeZone(Location location) {
        Faction faction = getFactionAt(location);
        String factionId = faction.getId();
        return Factions.ID_SAFEZONE.equals(factionId);
    }
    
    @Override
    public boolean isWarZone(Location location) {
        Faction faction = getFactionAt(location);
        String factionId = faction.getId();
        return Factions.ID_WARZONE.equals(factionId);
    }
    
    @Override
    public boolean isWilderness(Location location) {
        Faction faction = getFactionAt(location);
        String factionId = faction.getId();
        return Factions.ID_NONE.equals(factionId);
    }
}