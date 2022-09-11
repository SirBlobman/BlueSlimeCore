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

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

public final class FactionsHandler_Massive extends FactionsHandler {
    public FactionsHandler_Massive(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public Faction getFactionAt(Location location) {
        BoardColl boardColl = BoardColl.get();
        PS massiveCoreLocation = PS.valueOf(location);
        return boardColl.getFactionAt(massiveCoreLocation);
    }

    @Override
    public String getFactionNameAt(Location location) {
        Faction locationFaction = getFactionAt(location);
        if(locationFaction == null) {
            return null;
        }

        return locationFaction.getName();
    }

    @Override
    public boolean isSafeZone(Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        String factionId = locationFaction.getId();
        return Factions.ID_SAFEZONE.equals(factionId);
    }

    @Override
    public boolean isWarZone(Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        String factionId = locationFaction.getId();
        return Factions.ID_WARZONE.equals(factionId);
    }

    @Override
    public boolean isWilderness(Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return true;
        }

        String locationFactionId = locationFaction.getId();
        return Factions.ID_NONE.equals(locationFactionId);
    }

    @Override
    public boolean hasFaction(OfflinePlayer player) {
        MPlayer factionPlayer = MPlayer.get(player);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.hasFaction();
    }

    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        MPlayer factionPlayer = MPlayer.get(player);
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

        return playerFaction.getName();
    }

    @Override
    public boolean isMemberOrAlly(OfflinePlayer player1, OfflinePlayer player2) {
        UUID playerId1 = player1.getUniqueId();
        UUID playerId2 = player2.getUniqueId();
        if (playerId1.equals(playerId2) || !hasFaction(player1) || !hasFaction(player2)) {
            return true;
        }

        MPlayer factionPlayer1 = MPlayer.get(player1);
        MPlayer factionPlayer2 = MPlayer.get(player2);

        Rel relation = factionPlayer1.getRelationTo(factionPlayer2);
        return (relation == Rel.ALLY || relation == Rel.MEMBER);
    }

    @Override
    public boolean isEnemy(OfflinePlayer player1, OfflinePlayer player2) {
        UUID playerId1 = player1.getUniqueId();
        UUID playerId2 = player2.getUniqueId();
        if (playerId1.equals(playerId2) || !hasFaction(player1) || !hasFaction(player2)) {
            return false;
        }

        MPlayer factionPlayer1 = MPlayer.get(player1);
        MPlayer factionPlayer2 = MPlayer.get(player2);

        Rel relation = factionPlayer1.getRelationTo(factionPlayer2);
        return (relation == Rel.ENEMY);
    }

    @Override
    public boolean hasBypass(OfflinePlayer player) {
        MPlayer factionPlayer = MPlayer.get(player);
        if(factionPlayer == null) {
            return false;
        }

        return factionPlayer.isOverriding();
    }

    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        MPlayer factionPlayer = MPlayer.get(player);
        if (factionPlayer == null) {
            return false;
        }

        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        Rel relation = factionPlayer.getRelationTo(locationFaction);
        return (relation == Rel.ENEMY);
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

        String playerFactionId = playerFaction.getId();
        String locationFactionId = locationFaction.getId();
        return playerFactionId.equals(locationFactionId);
    }

    @Override
    public boolean isLeader(OfflinePlayer player, Location location) {
        Faction locationFaction = getFactionAt(location);
        if (locationFaction == null) {
            return false;
        }

        MPlayer locationFactionLeader = locationFaction.getLeader();
        if (locationFactionLeader == null) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        UUID leaderId = locationFactionLeader.getUuid();
        return playerId.equals(leaderId);
    }

    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        MPlayer factionPlayer = MPlayer.get(player);
        if (factionPlayer == null) {
            return false;
        }

        MPerm buildPermission = MPerm.getPermBuild();
        PS massiveCoreLocation = PS.valueOf(location);
        return buildPermission.has(factionPlayer, massiveCoreLocation, false);
    }

    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        return canBuild(player, location);
    }

    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        MPlayer factionPlayer = MPlayer.get(player);
        MPlayer factionViewer = MPlayer.get(viewer);
        if (factionPlayer == null || factionViewer == null) {
            return null;
        }

        Rel relation = factionViewer.getRelationTo(factionPlayer);
        return relation.getColor();
    }

    @Override
    public String getRolePrefix(OfflinePlayer player) {
        MPlayer factionPlayer = MPlayer.get(player);
        if (factionPlayer == null) {
            return null;
        }

        Rel role = factionPlayer.getRole();
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

        List<MPlayer> memberList = faction.getMPlayers();
        Set<UUID> memberIdSet = new HashSet<>();

        for (MPlayer member : memberList) {
            UUID memberId = member.getUuid();
            memberIdSet.add(memberId);
        }

        return Collections.unmodifiableSet(memberIdSet);
    }
}
