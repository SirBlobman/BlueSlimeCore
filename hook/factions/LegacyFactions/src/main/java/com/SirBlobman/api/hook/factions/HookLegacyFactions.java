package com.SirBlobman.api.hook.factions;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.redstoneore.legacyfactions.Factions;
import net.redstoneore.legacyfactions.Relation;
import net.redstoneore.legacyfactions.Role;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.locality.Locality;
import net.redstoneore.legacyfactions.locality.LocalityOwnership;

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
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? "" : faction.getTag());
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        FPlayer fplayer = FPlayerColl.get(player);
        return (fplayer.hasFaction() ? fplayer.getFaction() : null);
    }
    
    @Override
    public String getFactionNameFor(Player player) {
        Faction faction = getFactionFor(player);
        return (faction == null ? "" : faction.getTag());
    }
    
    @Override
    public boolean hasFaction(Player player) {
        FPlayer fplayer = FPlayerColl.get(player);
        return fplayer.hasFaction();
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
    
    @Override
    public boolean isMemberOrAlly(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2)) return true;
        if(!hasFaction(player1) || !hasFaction(player2)) return false;
        
        FPlayer fplayer1 = FPlayerColl.get(player1);
        FPlayer fplayer2 = FPlayerColl.get(player2);
    
        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation == Relation.ALLY || relation == Relation.MEMBER);
    }
    
    @Override
    public boolean isEnemy(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2)) return false;
        if(!hasFaction(player1) || !hasFaction(player2)) return false;
        
        FPlayer fplayer1 = FPlayerColl.get(player1);
        FPlayer fplayer2 = FPlayerColl.get(player2);
    
        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean hasBypass(Player player) {
        FPlayer fplayer = FPlayerColl.get(player);
        return fplayer.isAdminBypassing();
    }
    
    @Override
    public boolean isEnemyLand(Player player, Location location) {
        FPlayer fplayer = FPlayerColl.get(player);
        Faction faction = getFactionAt(location);
        Relation relation = fplayer.getRelationTo(faction);
        return (relation == Relation.ENEMY);
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
        FPlayer fowner = faction.getOwner();
        if(fowner == null) return false;
        
        UUID playerUUID = player.getUniqueId();
        String playerId = playerUUID.toString();
        String fownerId = fowner.getId();
        return fownerId.equals(playerId);
    }
    
    @Override
    public boolean canBuild(Player player, Location location) {
        Locality locality = Locality.of(location);
        LocalityOwnership ownership = locality.getOwnership();
        
        FPlayer fplayer = FPlayerColl.get(player);
        return ownership.hasAccess(fplayer);
    }
    
    @Override
    public ChatColor getRelationChatColor(Player viewer, Player player) {
        FPlayer fplayer = FPlayerColl.get(player);
        FPlayer fviewer = FPlayerColl.get(viewer);
        Relation relation = fviewer.getRelationTo(fplayer);
        return relation.getColor();
    }
    
    @Override
    public String getRolePrefix(Player player) {
        FPlayer fplayer = FPlayerColl.get(player);
        Role role = fplayer.getRole();
        return role.getPrefix();
    }
    
    @Override
    public List<UUID> getMembersForFactionAt(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return Collections.emptyList();
        
        Set<FPlayer> memberSet = faction.getFPlayers();
        return memberSet.stream().map(FPlayer::getId).map(UUID::fromString).collect(Collectors.toList());
    }
    
    @Override
    public List<UUID> getMembersForFactionOf(Player player) {
        Faction faction = getFactionFor(player);
        if(faction == null) return Collections.emptyList();
        
        Set<FPlayer> memberSet = faction.getFPlayers();
        return memberSet.stream().map(FPlayer::getId).map(UUID::fromString).collect(Collectors.toList());
    }
}