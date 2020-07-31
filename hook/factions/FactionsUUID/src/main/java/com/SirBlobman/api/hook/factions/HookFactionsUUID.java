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

import com.massivecraft.factions.*;
import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;

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
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? "" : faction.getTag());
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
        return (fplayer.hasFaction() ? fplayer.getFaction() : null);
    }
    
    @Override
    public String getFactionNameFor(Player player) {
        Faction faction = getFactionFor(player);
        return (faction == null ? "" : faction.getTag());
    }
    
    @Override
    public boolean hasFaction(Player player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
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
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer1 = fplayers.getByPlayer(player1);
        FPlayer fplayer2 = fplayers.getByPlayer(player2);
        
        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation == Relation.ALLY || relation == Relation.MEMBER);
    }
    
    @Override
    public boolean isEnemy(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2)) return false;
        if(!hasFaction(player1) || !hasFaction(player2)) return false;
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer1 = fplayers.getByPlayer(player1);
        FPlayer fplayer2 = fplayers.getByPlayer(player2);
        
        Relation relation = fplayer1.getRelationTo(fplayer2);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean hasBypass(Player player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
        return fplayer.isAdminBypassing();
    }
    
    @Override
    public boolean isEnemyLand(Player player, Location location) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
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
        List<FPlayer> adminList = faction.getFPlayersWhereRole(Role.ADMIN);
        if(adminList.isEmpty()) return false;
        
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
        return adminList.contains(fplayer);
    }
    
    @Override
    public boolean canBuild(Player player, Location location) {
        return FactionsBlockListener.playerCanBuildDestroyBlock(player, location, PermissibleAction.BUILD, true);
    }
    
    @Override
    public ChatColor getRelationChatColor(Player viewer, Player player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
        FPlayer fviewer = fplayers.getByPlayer(viewer);
        Relation relation = fviewer.getRelationTo(fplayer);
        return relation.getColor();
    }
    
    @Override
    public String getRolePrefix(Player player) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByPlayer(player);
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