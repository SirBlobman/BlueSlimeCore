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
        PS pos = PS.valueOf(location);
        BoardColl boardColl = BoardColl.get();
        return boardColl.getFactionAt(pos);
    }

    @Override
    public String getFactionNameAt(Location location) {
        Faction faction = getFactionAt(location);
        return (faction == null ? null : faction.getName());
    }

    @Override
    public boolean isSafeZone(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return false;

        String factionId = faction.getId();
        return Factions.ID_SAFEZONE.equals(factionId);
    }

    @Override
    public boolean isWarZone(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return false;

        String factionId = faction.getId();
        return Factions.ID_WARZONE.equals(factionId);
    }

    @Override
    public boolean isWilderness(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return true;

        String factionId = faction.getId();
        return Factions.ID_NONE.equals(factionId);
    }

    @Override
    public boolean hasFaction(OfflinePlayer player) {
        MPlayer mplayer = MPlayer.get(player);
        return (mplayer != null && mplayer.hasFaction());
    }

    @Override
    public Faction getFactionFor(OfflinePlayer player) {
        MPlayer mplayer = MPlayer.get(player);
        return (mplayer == null ? null : mplayer.getFaction());
    }

    @Override
    public String getFactionNameFor(OfflinePlayer player) {
        Faction faction = getFactionFor(player);
        return (faction == null ? null : faction.getName());
    }

    @Override
    public boolean isMemberOrAlly(OfflinePlayer player1, OfflinePlayer player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2) || !hasFaction(player1) || !hasFaction(player2)) {
            return true;
        }

        MPlayer mplayer1 = MPlayer.get(player1);
        MPlayer mplayer2 = MPlayer.get(player2);

        Rel relation = mplayer1.getRelationTo(mplayer2);
        return (relation == Rel.ALLY || relation == Rel.MEMBER);
    }

    @Override
    public boolean isEnemy(OfflinePlayer player1, OfflinePlayer player2) {
        UUID uuid1 = player1.getUniqueId();
        UUID uuid2 = player2.getUniqueId();
        if(uuid1.equals(uuid2) || !hasFaction(player1) || !hasFaction(player2)) {
            return false;
        }

        MPlayer mplayer1 = MPlayer.get(player1);
        MPlayer mplayer2 = MPlayer.get(player2);

        Rel relation = mplayer1.getRelationTo(mplayer2);
        return (relation == Rel.ENEMY);
    }

    @Override
    public boolean hasBypass(OfflinePlayer player) {
        MPlayer mplayer = MPlayer.get(player);
        return (mplayer != null && mplayer.isOverriding());
    }

    @Override
    public boolean isInEnemyLand(OfflinePlayer player, Location location) {
        MPlayer mplayer = MPlayer.get(player);
        if(mplayer == null) return false;

        Faction faction = getFactionAt(location);
        if(faction == null) return false;

        Rel relation = mplayer.getRelationTo(faction);
        return (relation == Rel.ENEMY);
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

        MPlayer mleader = faction.getLeader();
        if(mleader == null) return false;

        UUID playerId = player.getUniqueId();
        UUID leaderId = mleader.getUuid();
        return playerId.equals(leaderId);
    }

    @Override
    public boolean canBuild(OfflinePlayer player, Location location) {
        MPlayer mplayer = MPlayer.get(player);
        if(mplayer == null) return false;

        PS pos = PS.valueOf(location);
        MPerm permBuild = MPerm.getPermBuild();
        return permBuild.has(mplayer, pos, false);
    }

    @Override
    public boolean canDestroy(OfflinePlayer player, Location location) {
        MPlayer mplayer = MPlayer.get(player);
        if(mplayer == null) return false;

        PS pos = PS.valueOf(location);
        MPerm permBuild = MPerm.getPermBuild();
        return permBuild.has(mplayer, pos, false);
    }

    @Override
    public ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player) {
        MPlayer mplayer = MPlayer.get(player);
        MPlayer mviewer = MPlayer.get(viewer);
        if(mplayer == null || mviewer == null) return null;

        Rel relation = mviewer.getRelationTo(mplayer);
        return relation.getColor();
    }

    @Override
    public String getRolePrefix(OfflinePlayer player) {
        MPlayer mplayer = MPlayer.get(player);
        if(mplayer == null) return null;

        Rel role = mplayer.getRole();
        return role.getPrefix();
    }

    @Override
    public Set<UUID> getMembersForFactionAt(Location location) {
        Faction faction = getFactionAt(location);
        if(faction == null) return Collections.emptySet();

        List<MPlayer> mplayerList = faction.getMPlayers();
        Set<UUID> memberIdSet = new HashSet<>();

        for(MPlayer mplayer : mplayerList) {
            UUID mplayerId = mplayer.getUuid();
            memberIdSet.add(mplayerId);
        }

        return Collections.unmodifiableSet(memberIdSet);
    }

    @Override
    public Set<UUID> getMembersForFactionOf(OfflinePlayer player) {
        Faction faction = getFactionFor(player);
        if(faction == null) return Collections.emptySet();

        List<MPlayer> mplayerList = faction.getMPlayers();
        Set<UUID> memberIdSet = new HashSet<>();

        for(MPlayer mplayer : mplayerList) {
            UUID mplayerId = mplayer.getUuid();
            memberIdSet.add(mplayerId);
        }

        return Collections.unmodifiableSet(memberIdSet);
    }
}
