package com.github.sirblobman.api.factions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import net.redstoneore.legacyfactions.Relation;
import net.redstoneore.legacyfactions.Role;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.locality.Locality;
import net.redstoneore.legacyfactions.locality.LocalityOwnership;

public final class FactionsHandler_Legacy extends FactionsHandler {
    public FactionsHandler_Legacy(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Faction getFactionAt(Location location) {
        Locality locality = Locality.of(location);
        return locality.getFactionHere();
    }
    
    @Override
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? null : faction.getTag());
    }
    
    @Override
    public boolean isSafeZone(Location location) {
        Faction faction = getFactionAt(location);
        return (faction != null && faction.isSafeZone());
    }
    
    @Override
    public boolean isWarZone(Location location) {
        Faction faction = getFactionAt(location);
        return (faction != null && faction.isWarZone());
    }
    
    @Override
    public boolean isWilderness(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null || faction.isWilderness());
    }
    
    @Override
    public boolean hasFaction(OfflinePlayer player) {
        FPlayer fplayer = FPlayerColl.get(player);
        return (fplayer != null && fplayer.hasFaction());
    }
    
    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        FPlayer fplayer = FPlayerColl.get(player);
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
        
        FPlayer fplayer1 = FPlayerColl.get(player1);
        FPlayer fplayer2 = FPlayerColl.get(player2);
        
        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation == Relation.ALLY || relation == Relation.MEMBER);
    }
    
    @Override
    public boolean isEnemy(OfflinePlayer player1, OfflinePlayer player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2) || !hasFaction(player1) || !hasFaction(player2)) {
            return false;
        }
        
        FPlayer fplayer1 = FPlayerColl.get(player1);
        FPlayer fplayer2 = FPlayerColl.get(player2);
        
        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean hasBypass(OfflinePlayer player) {
        FPlayer fplayer = FPlayerColl.get(player);
        return (fplayer != null && fplayer.isAdminBypassing());
    }
    
    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        FPlayer fplayer = FPlayerColl.get(player);
        if(fplayer == null) return false;
        
        Faction faction = getFactionAt(location);
        if(faction == null) return false;
        
        Relation relation = fplayer.getRelationTo(faction);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean isInOwnFaction(OfflinePlayer player, Location location) {
        if(!hasFaction(player)) return false;
        
        Faction factionPlayer = getFactionFor(player);
        if(factionPlayer == null) return false;
        
        Faction factionLocation = getFactionAt(location);
        if(factionLocation == null) return false;
        
        String factionPlayerId = factionPlayer.getId();
        String factionLocationId = factionLocation.getId();
        return factionPlayerId.equals(factionLocationId);
    }
    
    @Override
    public boolean isLeader(OfflinePlayer player, Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return false;
        
        FPlayer fowner = faction.getOwner();
        if(fowner == null) return false;
        
        UUID playerId = player.getUniqueId();
        String playerIdString = playerId.toString();
        String ownerIdString = fowner.getId();
        return playerIdString.equals(ownerIdString);
    }
    
    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        FPlayer fplayer = FPlayerColl.get(player);
        if(fplayer == null) return false;
        
        Locality locality = Locality.of(location);
        LocalityOwnership ownership = locality.getOwnership();
        return ownership.hasAccess(fplayer);
    }
    
    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        FPlayer fplayer = FPlayerColl.get(player);
        if(fplayer == null) return false;
        
        Locality locality = Locality.of(location);
        LocalityOwnership ownership = locality.getOwnership();
        return ownership.hasAccess(fplayer);
    }
    
    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        FPlayer fplayer = FPlayerColl.get(player);
        FPlayer fviewer = FPlayerColl.get(viewer);
        if(fplayer == null || fviewer == null) return null;
        
        Relation relation = fviewer.getRelationTo(fplayer);
        return relation.getColor();
    }
    
    @Override
    public String getRolePrefix(OfflinePlayer player) {
        FPlayer fplayer = FPlayerColl.get(player);
        if(fplayer == null) return null;
        
        Role role = fplayer.getRole();
        return role.getPrefix();
    }
    
    @Override
    public Set<UUID> getMembersForFactionAt(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return Collections.emptySet();
        
        Set<FPlayer> memberSet = faction.getMembers();
        Set<UUID> memberIdSet = new HashSet<>();
        
        for(FPlayer fplayer : memberSet) {
            String fplayerIdString = fplayer.getId();
            UUID fplayerId = UUID.fromString(fplayerIdString);
            memberIdSet.add(fplayerId);
        }
        
        return Collections.unmodifiableSet(memberIdSet);
    }
    
    @Override
    public Set<UUID> getMembersForFactionOf(OfflinePlayer player) {
        Faction faction = getFactionFor(player);
        if(faction == null) return Collections.emptySet();
        
        Set<FPlayer> memberSet = faction.getMembers();
        Set<UUID> memberIdSet = new HashSet<>();
        
        for(FPlayer fplayer : memberSet) {
            String fplayerIdString = fplayer.getId();
            UUID fplayerId = UUID.fromString(fplayerIdString);
            memberIdSet.add(fplayerId);
        }
        
        return Collections.unmodifiableSet(memberIdSet);
    }
}
