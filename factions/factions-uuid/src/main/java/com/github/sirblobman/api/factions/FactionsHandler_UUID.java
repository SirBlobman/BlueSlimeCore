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
        FLocation factionLocation = new FLocation(location);
        return board.getFactionAt(factionLocation);
    }

    @Override
    public String getFactionNameAt(Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return "";
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
        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.hasFaction();
    }

    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
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

        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer1 = factionPlayers.getByOfflinePlayer(player1);
        FPlayer factionPlayer2 = factionPlayers.getByOfflinePlayer(player2);

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

        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer1 = factionPlayers.getByOfflinePlayer(player1);
        FPlayer factionPlayer2 = factionPlayers.getByOfflinePlayer(player2);

        Relation relation = factionPlayer1.getRelationTo(factionPlayer2);
        return (relation == Relation.ENEMY);
    }

    @Override
    public boolean hasBypass(OfflinePlayer player) {
        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.isAdminBypassing();
    }

    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
        Faction locationFaction = getFactionAt(location);

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

        List<FPlayer> adminList = locationFaction.getFPlayersWhereRole(Role.ADMIN);
        if (adminList.isEmpty()) {
            return false;
        }

        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
        return adminList.contains(factionPlayer);
    }

    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return true;
        }

        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
        if (factionPlayer == null) {
            return false;
        }

        FLocation factionLocation = new FLocation(location);
        return locationFaction.hasAccess(factionPlayer, PermissibleActions.BUILD, factionLocation);
    }

    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return true;
        }

        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
        if (factionPlayer == null) {
            return false;
        }

        FLocation factionLocation = new FLocation(location);
        return locationFaction.hasAccess(factionPlayer, PermissibleActions.DESTROY, factionLocation);
    }

    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
        FPlayer factionViewer = factionPlayers.getByOfflinePlayer(viewer);
        if (factionPlayer == null || factionViewer == null) {
            return null;
        }

        Relation relation = factionViewer.getRelationTo(factionPlayer);
        return relation.getColor();
    }

    @Override
    public String getRolePrefix(OfflinePlayer player) {
        FPlayers factionPlayers = FPlayers.getInstance();
        FPlayer factionPlayer = factionPlayers.getByOfflinePlayer(player);
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

        Set<FPlayer> memberSet = faction.getFPlayers();
        Set<UUID> memberIdSet = new HashSet<>();

        for (FPlayer member : memberSet) {
            String memberIdString = member.getId();
            UUID memberId = UUID.fromString(memberIdString);
            memberIdSet.add(memberId);
        }

        return Collections.unmodifiableSet(memberIdSet);
    }
}
