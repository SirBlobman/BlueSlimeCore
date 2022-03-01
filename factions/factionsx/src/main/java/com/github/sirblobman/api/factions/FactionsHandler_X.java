package com.github.sirblobman.api.factions;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import net.prosavage.factionsx.core.CustomRole;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.util.Relation;

public final class FactionsHandler_X extends FactionsHandler {
    public FactionsHandler_X(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Faction getFactionAt(Location location) {
        Chunk chunk = location.getChunk();
        return GridManager.INSTANCE.getFactionAt(chunk);
    }
    
    @Override
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? null : faction.getTag());
    }
    
    @Override
    public boolean isSafeZone(Location location) {
        Faction faction = getFactionAt(location);
        return (faction != null && faction.isSafezone());
    }
    
    @Override
    public boolean isWarZone(Location location) {
        Faction faction = getFactionAt(location);
        return (faction != null && faction.isWarzone());
    }
    
    @Override
    public boolean isWilderness(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null || faction.isWilderness());
    }
    
    @Override
    public boolean hasFaction(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(uuid);
        return (fplayer != null && fplayer.hasFaction());
    }
    
    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(uuid);
        return (fplayer == null ? null : fplayer.getFaction());
    }
    
    @Override
    public String getFactionNameFor(OfflinePlayer player) {
        Faction faction = getFactionFor(player);
        return (faction == null ? null : faction.getTag());
    }
    
    @Override
    public boolean isMemberOrAlly(OfflinePlayer player1, OfflinePlayer player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2) || !hasFaction(player1) || !hasFaction(player2)) {
            return true;
        }
        
        FPlayer fplayer1 = PlayerManager.INSTANCE.getFPlayer(uuid1);
        FPlayer fplayer2 = PlayerManager.INSTANCE.getFPlayer(uuid2);
        
        Faction faction1 = fplayer1.getFaction();
        Faction faction2 = fplayer2.getFaction();
        
        long factionId1 = faction1.getId();
        long factionId2 = faction2.getId();
        if(factionId1 == factionId2) return true;
        
        Relation relation = faction1.getRelationTo(faction2);
        return (relation == Relation.ALLY);
    }
    
    @Override
    public boolean isEnemy(OfflinePlayer player1, OfflinePlayer player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2) || !hasFaction(player1) || !hasFaction(player2)) {
            return false;
        }
        
        FPlayer fplayer1 = PlayerManager.INSTANCE.getFPlayer(uuid1);
        FPlayer fplayer2 = PlayerManager.INSTANCE.getFPlayer(uuid2);
        
        Faction faction1 = fplayer1.getFaction();
        Faction faction2 = fplayer2.getFaction();
        
        long factionId1 = faction1.getId();
        long factionId2 = faction2.getId();
        if(factionId1 == factionId2) return true;
        
        Relation relation = faction1.getRelationTo(faction2);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean hasBypass(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(uuid);
        return (fplayer != null && fplayer.getInBypass());
    }
    
    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        if(!hasFaction(player)) return false;
        
        UUID uuid = player.getUniqueId();
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(uuid);
        
        Faction fplayerFaction = fplayer.getFaction();
        Faction faction = getFactionAt(location);
        
        Relation relation = fplayerFaction.getRelationTo(faction);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean isInOwnFaction(OfflinePlayer player, Location location) {
        if(!hasFaction(player)) return false;
        
        Faction factionPlayer = getFactionFor(player);
        if(factionPlayer == null) return false;
        
        Faction factionLocation = getFactionAt(location);
        if(factionLocation == null) return false;
        
        long factionPlayerId = factionPlayer.getId();
        long factionLocationId = factionLocation.getId();
        return (factionPlayerId == factionLocationId);
    }
    
    @Override
    public boolean isLeader(OfflinePlayer player, Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return false;
        
        UUID playerId = player.getUniqueId();
        UUID ownerId = faction.getOwnerId();
        return playerId.equals(ownerId);
    }
    
    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        UUID uuid = player.getUniqueId();
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(uuid);
        return (fplayer != null && fplayer.canBuildAt(location));
    }
    
    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        UUID uuid = player.getUniqueId();
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(uuid);
        return (fplayer != null && fplayer.canBreakAt(location));
    }
    
    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        if(!hasFaction(viewer) || !hasFaction(player)) return null;
        
        Faction viewerFaction = getFactionFor(viewer);
        Faction playerFaction = getFactionFor(player);
        if(viewerFaction == null || playerFaction == null) return null;
        
        Relation relation = viewerFaction.getRelationTo(playerFaction);
        switch(relation) {
            case ALLY:
                return ChatColor.LIGHT_PURPLE;
            case ENEMY:
                return ChatColor.RED;
            case TRUCE:
                return ChatColor.YELLOW;
            case NEUTRAL:
                return ChatColor.WHITE;
            default:
                break;
        }
        
        return null;
    }
    
    @Override
    public String getRolePrefix(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(uuid);
        if(fplayer == null) return null;
        
        CustomRole role = fplayer.getRole();
        return role.getChatTag();
    }
    
    @Override
    public Set<UUID> getMembersForFactionAt(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return Collections.emptySet();
        
        Set<UUID> factionMemberSet = faction.getFactionMembers();
        return Collections.unmodifiableSet(factionMemberSet);
    }
    
    @Override
    public Set<UUID> getMembersForFactionOf(OfflinePlayer player) {
        Faction faction = getFactionFor(player);
        if(faction == null) return Collections.emptySet();
        
        Set<UUID> factionMemberSet = faction.getFactionMembers();
        return Collections.unmodifiableSet(factionMemberSet);
    }
}
