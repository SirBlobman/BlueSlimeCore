package com.SirBlobman.api.hook.factions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.prosavage.factionsx.FactionsX;
import net.prosavage.factionsx.core.CustomRole;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.util.Relation;

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
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? "" : faction.getTag());
    }
    
    @Override
    public Faction getFactionFor(Player player) {
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(player);
        return (fplayer.hasFaction() ? fplayer.getFaction() : null);
    }
    
    @Override
    public String getFactionNameFor(Player player) {
        Faction faction = getFactionFor(player);
        return (faction == null ? "" : faction.getTag());
    }
    
    @Override
    public boolean hasFaction(Player player) {
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(player);
        return fplayer.hasFaction();
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
    
    @Override
    public boolean isMemberOrAlly(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2)) return true;
        if(!hasFaction(player1) || !hasFaction(player2)) return false;
        
        FPlayer fplayer1 = PlayerManager.INSTANCE.getFPlayer(player1);
        FPlayer fplayer2 = PlayerManager.INSTANCE.getFPlayer(player2);
        
        Faction faction1 = fplayer1.getFaction();
        Faction faction2 = fplayer2.getFaction();
    
        long factionId1 = faction1.getId();
        long factionId2 = faction2.getId();
        if(factionId1 == factionId2) return true;
    
        Relation relation = faction1.getRelationTo(faction2);
        return (relation == Relation.ALLY);
    }
    
    @Override
    public boolean isEnemy(Player player1, Player player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2)) return false;
        if(!hasFaction(player1) || !hasFaction(player2)) return false;
        
        FPlayer fplayer1 = PlayerManager.INSTANCE.getFPlayer(player1);
        FPlayer fplayer2 = PlayerManager.INSTANCE.getFPlayer(player2);
        
        Faction faction1 = fplayer1.getFaction();
        Faction faction2 = fplayer2.getFaction();
    
        long factionId1 = faction1.getId();
        long factionId2 = faction2.getId();
        if(factionId1 == factionId2) return false;
    
        Relation relation = faction1.getRelationTo(faction2);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean hasBypass(Player player) {
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(player);
        return fplayer.getInBypass();
    }
    
    @Override
    public boolean isEnemyLand(Player player, Location location) {
        if(!hasFaction(player)) return false;
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(player);
        
        Faction fplayerFaction = fplayer.getFaction();
        Faction faction = getFactionAt(location);
        
        Relation relation = fplayerFaction.getRelationTo(faction);
        return (relation == Relation.ENEMY);
    }
    
    @Override
    public boolean isInOwnFaction(Player player, Location location) {
        if(!hasFaction(player)) return false;
        
        Faction playerFaction = getFactionFor(player);
        Faction locationFaction = getFactionAt(location);
        
        long playerFactionId = playerFaction.getId();
        long locationFactionId = locationFaction.getId();
        return (playerFactionId == locationFactionId);
    }
    
    @Override
    public boolean isLeader(Player player, Location location) {
        Faction faction = getFactionAt(location);
        UUID ownerId = faction.getOwnerId();
        if(ownerId == null) return false;
        
        UUID playerId = player.getUniqueId();
        return playerId.equals(ownerId);
    }
    
    @Override
    public boolean canBuild(Player player, Location location) {
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(player);
        return fplayer.canBuildAt(location);
    }
    
    @Override
    public ChatColor getRelationChatColor(Player viewer, Player player) {
        if(!hasFaction(viewer) || !hasFaction(player)) return ChatColor.RESET;
        
        Faction viewerFaction = getFactionFor(viewer);
        Faction playerFaction = getFactionFor(player);
        Relation relation = viewerFaction.getRelationTo(playerFaction);
        
        switch(relation) {
            case ALLY: return ChatColor.LIGHT_PURPLE;
            case ENEMY: return ChatColor.RED;
            case TRUCE: return ChatColor.YELLOW;
            case NEUTRAL: return ChatColor.WHITE;
            default: break;
        }
        
        return ChatColor.RESET;
    }
    
    @Override
    public String getRolePrefix(Player player) {
        FPlayer fplayer = PlayerManager.INSTANCE.getFPlayer(player);
        CustomRole role = fplayer.getRole();
        return role.getChatTag();
    }
    
    @Override
    public List<UUID> getMembersForFactionAt(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return Collections.emptyList();
        return new ArrayList<>(faction.getFactionMembers());
    }
    
    @Override
    public List<UUID> getMembersForFactionOf(Player player) {
        Faction faction = getFactionFor(player);
        if(faction == null) return Collections.emptyList();
        return new ArrayList<>(faction.getFactionMembers());
    }
}