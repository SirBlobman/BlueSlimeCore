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
        Faction locationFaction = getFactionAt(location);
        if(locationFaction == null) {
            return null;
        }

        return locationFaction.getTag();
    }

    @Override
    public boolean isSafeZone(Location location) {
        Faction locationFaction = getFactionAt(location);
        if(locationFaction == null) {
            return false;
        }

        return locationFaction.isSafezone();
    }

    @Override
    public boolean isWarZone(Location location) {
        Faction locationFaction = getFactionAt(location);
        if(locationFaction == null) {
            return false;
        }

        return locationFaction.isWarzone();
    }

    @Override
    public boolean isWilderness(Location location) {
        Faction locationFaction = getFactionAt(location);
        if(locationFaction == null) {
            return true;
        }

        return locationFaction.isWilderness();
    }

    @Override
    public boolean hasFaction(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.hasFaction();
    }

    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if(factionPlayer == null) {
            return null;
        }

        return factionPlayer.getFaction();
    }

    @Override
    public String getFactionNameFor(OfflinePlayer player) {
        Faction playerFaction = getFactionFor(player);
        if(playerFaction == null) {
            return null;
        }

        return playerFaction.getTag();
    }

    @Override
    public boolean isMemberOrAlly(OfflinePlayer player1, OfflinePlayer player2) {
        UUID playerId1 = player1.getUniqueId();
        UUID playerId2 = player2.getUniqueId();
        if (playerId1.equals(playerId2) || !hasFaction(player1) || !hasFaction(player2)) {
            return true;
        }

        FPlayer factionPlayer1 = PlayerManager.INSTANCE.getFPlayer(playerId1);
        FPlayer factionPlayer2 = PlayerManager.INSTANCE.getFPlayer(playerId2);

        Faction playerFaction1 = factionPlayer1.getFaction();
        Faction playerFaction2 = factionPlayer2.getFaction();

        long factionId1 = playerFaction1.getId();
        long factionId2 = playerFaction2.getId();
        if (factionId1 == factionId2) {
            return true;
        }

        Relation relation = playerFaction1.getRelationTo(playerFaction2);
        return (relation == Relation.ALLY);
    }

    @Override
    public boolean isEnemy(OfflinePlayer player1, OfflinePlayer player2) {
        UUID playerId1 = player1.getUniqueId();
        UUID playerId2 = player2.getUniqueId();
        if (playerId1.equals(playerId2) || !hasFaction(player1) || !hasFaction(player2)) {
            return false;
        }

        FPlayer factionPlayer1 = PlayerManager.INSTANCE.getFPlayer(playerId1);
        FPlayer factionPlayer2 = PlayerManager.INSTANCE.getFPlayer(playerId2);

        Faction playerFaction1 = factionPlayer1.getFaction();
        Faction playerFaction2 = factionPlayer2.getFaction();

        long factionId1 = playerFaction1.getId();
        long factionId2 = playerFaction2.getId();
        if (factionId1 == factionId2) {
            return false;
        }

        Relation relation = playerFaction1.getRelationTo(playerFaction2);
        return (relation == Relation.ENEMY);
    }

    @Override
    public boolean hasBypass(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.getInBypass();
    }

    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        if (!hasFaction(player)) {
            return false;
        }

        Faction playerFaction = getFactionFor(player);
        Faction locationFaction = getFactionAt(location);
        if(playerFaction == null || locationFaction == null) {
            return false;
        }

        Relation relation = playerFaction.getRelationTo(locationFaction);
        return (relation == Relation.ENEMY);
    }

    @Override
    public boolean isInOwnFaction(OfflinePlayer player, Location location) {
        if (!hasFaction(player)) {
            return false;
        }

        Faction playerFaction = getFactionFor(player);
        Faction locationFaction = getFactionAt(location);
        if (playerFaction == null || locationFaction == null) {
            return false;
        }

        long factionPlayerId = playerFaction.getId();
        long factionLocationId = locationFaction.getId();
        return (factionPlayerId == factionLocationId);
    }

    @Override
    public boolean isLeader(OfflinePlayer player, Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        UUID ownerId = locationFaction.getOwnerId();
        return playerId.equals(ownerId);
    }

    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.canBuildAt(location);
    }

    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.canBreakAt(location);
    }

    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        if (!hasFaction(viewer) || !hasFaction(player)) {
            return null;
        }

        Faction viewerFaction = getFactionFor(viewer);
        Faction playerFaction = getFactionFor(player);
        if (viewerFaction == null || playerFaction == null) {
            return null;
        }

        Relation relation = viewerFaction.getRelationTo(playerFaction);
        switch (relation) {
            case ALLY: return ChatColor.LIGHT_PURPLE;
            case ENEMY: return ChatColor.RED;
            case TRUCE: return ChatColor.YELLOW;
            case NEUTRAL: return ChatColor.WHITE;
            default: break;
        }

        return null;
    }

    @Override
    public String getRolePrefix(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if (factionPlayer == null) {
            return null;
        }

        CustomRole role = factionPlayer.getRole();
        return role.getChatTag();
    }

    @Override
    public Set<UUID> getMembersForFactionAt(Location location) {
        Faction locationFaction = getFactionAt(location);
        return getMembersForFaction(locationFaction);
    }

    @Override
    public Set<UUID> getMembersForFactionOf(OfflinePlayer player) {
        Faction playerFaction = getFactionFor(player);
        return getMembersForFaction(playerFaction);
    }

    private Set<UUID> getMembersForFaction(Faction faction) {
        if (faction == null) {
            return Collections.emptySet();
        }

        Set<UUID> factionMemberSet = faction.getFactionMembers();
        return Collections.unmodifiableSet(factionMemberSet);
    }
}
