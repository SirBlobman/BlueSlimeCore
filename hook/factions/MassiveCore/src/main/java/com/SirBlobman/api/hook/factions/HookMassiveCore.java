package com.SirBlobman.api.hook.factions;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
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
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? "" : faction.getName());
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        MPlayer mplayer = MPlayer.get(player);
        return (mplayer.hasFaction() ? mplayer.getFaction() : null);
    }
    
    @Override
    public String getFactionNameFor(Player player) {
        Faction faction = getFactionFor(player);
        return (faction == null ? "" : faction.getName());
    }
    
    @Override
    public boolean hasFaction(Player player) {
        if(player == null) return false;
        
        MPlayer mplayer = MPlayer.get(player);
        return (mplayer != null && mplayer.hasFaction());
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
    
    @Override
    public boolean isMemberOrAlly(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2)) return true;
        if(!hasFaction(player1) || !hasFaction(player2)) return false;
        
        MPlayer mplayer1 = MPlayer.get(player1);
        MPlayer mplayer2 = MPlayer.get(player2);
        
        Rel relation = mplayer1.getRelationTo(mplayer2);
        return relation.isAtLeast(Rel.ALLY);
    }
    
    @Override
    public boolean isEnemy(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2)) return false;
        if(!hasFaction(player1) || !hasFaction(player2)) return false;
        
        MPlayer mplayer1 = MPlayer.get(player1);
        MPlayer mplayer2 = MPlayer.get(player2);
        
        Rel relation = mplayer1.getRelationTo(mplayer2);
        return (relation == Rel.ENEMY);
    }
    
    @Override
    public boolean hasBypass(Player player) {
        MPlayer mplayer = MPlayer.get(player);
        return mplayer.isOverriding();
    }
    
    @Override
    public boolean isEnemyLand(Player player, Location location) {
        MPlayer mplayer = MPlayer.get(player);
        Faction faction = getFactionAt(location);
        Rel relation = mplayer.getRelationTo(faction);
        return (relation == Rel.ENEMY);
    }
    
    @Override
    public boolean isInOwnFaction(Player player, Location location) {
        if(!hasFaction(player)) return false;
        
        Faction playerFaction = getFactionFor(player);
        Faction locationFaction = getFactionAt(location);
        
        String playerFactionId = playerFaction.getId();
        String locationFactionId = locationFaction.getId();
        return playerFactionId.equals(locationFactionId);
    }
    
    @Override
    public boolean isLeader(Player player, Location location) {
        Faction faction = getFactionAt(location);
        MPlayer mplayer = faction.getLeader();
        if(mplayer == null) return false;
    
        Player mplayerPlayer = mplayer.getPlayer();
        if(mplayerPlayer == null) return false;
        
        UUID mplayerUUID = mplayerPlayer.getUniqueId();
        UUID playerUUID = player.getUniqueId();
        return playerUUID.equals(mplayerUUID);
    }
    
    @Override
    public boolean canBuild(Player player, Location location) {
        PS ps = PS.valueOf(location);
        MPerm permBuild = MPerm.getPermBuild();
        MPlayer mplayer = MPlayer.get(player);
        return permBuild.has(mplayer, ps, false);
    }
    
    @Override
    public ChatColor getRelationChatColor(Player viewer, Player player) {
        MPlayer mplayer = MPlayer.get(player);
        MPlayer mviewer = MPlayer.get(viewer);
        Rel relation = mviewer.getRelationTo(mplayer);
        return relation.getColor();
    }
    
    @Override
    public String getRolePrefix(Player player) {
        MPlayer mplayer = MPlayer.get(player);
        Rel role = mplayer.getRole();
        return role.getPrefix();
    }
    
    @Override
    public List<UUID> getMembersForFactionAt(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return Collections.emptyList();
    
        List<MPlayer> mplayerList = faction.getMPlayers();
        return mplayerList.stream().map(MPlayer::getUuid).collect(Collectors.toList());
    }
    
    @Override
    public List<UUID> getMembersForFactionOf(Player player) {
        Faction faction = getFactionFor(player);
        if(faction == null) return Collections.emptyList();
    
        List<MPlayer> mplayerList = faction.getMPlayers();
        return mplayerList.stream().map(MPlayer::getUuid).collect(Collectors.toList());
    }
}