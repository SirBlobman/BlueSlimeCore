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
        Locality factionLocation = Locality.of(location);
        return factionLocation.getFactionHere();
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

        return locationFaction.isSafeZone();
    }

    @Override
    public boolean isWarZone(Location location) {
        Faction locationFaction = getFactionAt(location);
        if(locationFaction == null) {
            return false;
        }

        return locationFaction.isWarZone();
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
        FPlayer factionPlayer = FPlayerColl.get(player);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.hasFaction();
    }

    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        FPlayer factionPlayer = FPlayerColl.get(player);
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

        FPlayer factionPlayer1 = FPlayerColl.get(player1);
        FPlayer factionPlayer2 = FPlayerColl.get(player2);

        Relation relation = factionPlayer1.getRelationTo(factionPlayer2);
        return (relation == Relation.ALLY || relation == Relation.MEMBER);
    }

    @Override
    public boolean isEnemy(OfflinePlayer player1, OfflinePlayer player2) {
        UUID playerId1 = player1.getUniqueId();
        UUID playerId2 = player2.getUniqueId();
        if (playerId1.equals(playerId2) || !hasFaction(player1) || !hasFaction(player2)) {
            return false;
        }

        FPlayer factionPlayer1 = FPlayerColl.get(player1);
        FPlayer factionPlayer2 = FPlayerColl.get(player2);

        Relation relation = factionPlayer1.getRelationTo(factionPlayer2);
        return (relation == Relation.ENEMY);
    }

    @Override
    public boolean hasBypass(OfflinePlayer player) {
        FPlayer factionPlayer = FPlayerColl.get(player);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.isAdminBypassing();
    }

    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        FPlayer factionPlayer = FPlayerColl.get(player);
        if (factionPlayer == null) {
            return false;
        }

        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        Relation relation = factionPlayer.getRelationTo(locationFaction);
        return (relation == Relation.ENEMY);
    }

    @Override
    public boolean isInOwnFaction(OfflinePlayer player, Location location) {
        if (!hasFaction(player)) {
            return false;
        }

        Faction playerFaction = getFactionFor(player);
        if (playerFaction == null) {
            return false;
        }

        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        String factionPlayerId = playerFaction.getId();
        String factionLocationId = locationFaction.getId();
        return factionPlayerId.equals(factionLocationId);
    }

    @Override
    public boolean isLeader(OfflinePlayer player, Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        FPlayer factionOwner = locationFaction.getOwner();
        if (factionOwner == null) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        String playerIdString = playerId.toString();
        String ownerIdString = factionOwner.getId();
        return playerIdString.equals(ownerIdString);
    }

    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        FPlayer factionPlayer = FPlayerColl.get(player);
        if (factionPlayer == null) {
            return false;
        }

        Locality factionLocation = Locality.of(location);
        LocalityOwnership ownership = factionLocation.getOwnership();
        return ownership.hasAccess(factionPlayer);
    }

    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        FPlayer factionPlayer = FPlayerColl.get(player);
        if (factionPlayer == null) {
            return false;
        }

        Locality factionLocation = Locality.of(location);
        LocalityOwnership ownership = factionLocation.getOwnership();
        return ownership.hasAccess(factionPlayer);
    }

    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        FPlayer factionPlayer = FPlayerColl.get(player);
        FPlayer factionViewer = FPlayerColl.get(viewer);
        if (factionPlayer == null || factionViewer == null) {
            return null;
        }

        Relation relation = factionViewer.getRelationTo(factionPlayer);
        return relation.getColor();
    }

    @Override
    public String getRolePrefix(OfflinePlayer player) {
        FPlayer factionPlayer = FPlayerColl.get(player);
        if (factionPlayer == null) {
            return null;
        }

        Role role = factionPlayer.getRole();
        return role.getPrefix();
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

        Set<FPlayer> memberSet = faction.getMembers();
        Set<UUID> memberIdSet = new HashSet<>();

        for (FPlayer member : memberSet) {
            String memberIdString = member.getId();
            UUID memberId = UUID.fromString(memberIdString);
            memberIdSet.add(memberId);
        }

        return Collections.unmodifiableSet(memberIdSet);
    }
}
