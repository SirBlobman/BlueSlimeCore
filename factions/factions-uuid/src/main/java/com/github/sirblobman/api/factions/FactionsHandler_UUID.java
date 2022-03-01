package com.github.sirblobman.api.factions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;

public final class FactionsHandler_UUID extends FactionsHandler {
    public FactionsHandler_UUID(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Faction getFactionAt(Location location) {
        Board board = Board.getInstance();
        FLocation flocation = new FLocation(location);
        return board.getFactionAt(flocation);
    }
    
    @Override
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? "" : faction.getTag());
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
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        return (fplayer != null && fplayer.hasFaction());
    }
    
    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
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
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer1 = fplayers.getByOfflinePlayer(player1);
        FPlayer fplayer2 = fplayers.getByOfflinePlayer(player2);
        
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
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer1 = fplayers.getByOfflinePlayer(player1);
        FPlayer fplayer2 = fplayers.getByOfflinePlayer(player2);
        
        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean hasBypass(OfflinePlayer player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        return (fplayer != null && fplayer.isAdminBypassing());
    }
    
    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        Faction faction = getFactionAt(location);
        
        Relation relation = fplayer.getRelationTo(faction);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean isInOwnFaction(OfflinePlayer player, Location location) {
        if(!hasFaction(player)) {
            return false;
        }
        
        Faction factionPlayer = getFactionFor(player);
        if(factionPlayer == null) {
            return false;
        }
        
        Faction factionLocation = getFactionAt(location);
        if(factionLocation == null) {
            return false;
        }
        
        String factionPlayerId = factionPlayer.getId();
        String factionLocationId = factionLocation.getId();
        return factionPlayerId.equals(factionLocationId);
    }
    
    @Override
    public boolean isLeader(OfflinePlayer player, Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) {
            return false;
        }
        
        List<FPlayer> adminList = faction.getFPlayersWhereRole(Role.ADMIN);
        if(adminList.isEmpty()) {
            return false;
        }
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        return adminList.contains(fplayer);
    }
    
    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) {
            return true;
        }
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        if(fplayer == null) {
            return false;
        }
        
        FLocation flocation = new FLocation(location);
        return faction.hasAccess(fplayer, PermissibleActions.BUILD, flocation);
    }
    
    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) {
            return true;
        }
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        if(fplayer == null) {
            return false;
        }
        
        FLocation flocation = new FLocation(location);
        return faction.hasAccess(fplayer, PermissibleActions.DESTROY, flocation);
    }
    
    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        FPlayer fviewer = fplayers.getByOfflinePlayer(viewer);
        if(fplayer == null || fviewer == null) {
            return null;
        }
        
        Relation relation = fviewer.getRelationTo(fplayer);
        return relation.getColor();
    }
    
    @Override
    public String getRolePrefix(OfflinePlayer player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        if(fplayer == null) {
            return null;
        }
        
        Role role = fplayer.getRole();
        return role.getPrefix();
    }
    
    @Override
    public Set<UUID> getMembersForFactionAt(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) {
            return Collections.emptySet();
        }
        
        Set<FPlayer> memberSet = faction.getFPlayers();
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
        
        Set<FPlayer> memberSet = faction.getFPlayers();
        Set<UUID> memberIdSet = new HashSet<>();
        
        for(FPlayer fplayer : memberSet) {
            String fplayerIdString = fplayer.getId();
            UUID fplayerId = UUID.fromString(fplayerIdString);
            memberIdSet.add(fplayerId);
        }
        
        return Collections.unmodifiableSet(memberIdSet);
    }
}
