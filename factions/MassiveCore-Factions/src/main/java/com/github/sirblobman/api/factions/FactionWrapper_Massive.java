package com.github.sirblobman.api.factions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.github.sirblobman.api.utility.Validate;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.jetbrains.annotations.NotNull;

public final class FactionWrapper_Massive extends FactionWrapper {
    private final Faction faction;

    public FactionWrapper_Massive(Faction faction) {
        this.faction = Validate.notNull(faction, "faction must not be null!");
    }

    private @NotNull Faction getFaction() {
        return this.faction;
    }

    @Override
    public @NotNull String getFactionId() {
        Faction faction = getFaction();
        return faction.getId();
    }

    @Override
    public @NotNull String getFactionName() {
        Faction faction = getFaction();
        return faction.getName();
    }

    @Override
    public boolean isSafeZone() {
        String factionId = getFactionId();
        return (factionId.equals(Factions.ID_SAFEZONE));
    }

    @Override
    public boolean isWarZone() {
        String factionId = getFactionId();
        return (factionId.equals(Factions.ID_WARZONE));
    }

    @Override
    public boolean isWilderness() {
        String factionId = getFactionId();
        return (factionId.equals(Factions.ID_NONE));
    }

    @Override
    public boolean isLeader(@NotNull OfflinePlayer player) {
        Faction faction = getFaction();
        MPlayer leader = faction.getLeader();
        if (leader == null) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        UUID leaderId = leader.getUuid();
        return playerId.equals(leaderId);
    }

    @Override
    public boolean isMember(@NotNull OfflinePlayer player) {
        Faction faction = getFaction();
        List<MPlayer> memberList = faction.getMPlayers();

        UUID playerId = player.getUniqueId();
        for (MPlayer member : memberList) {
            UUID memberId = member.getUuid();
            if (playerId.equals(memberId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canPlaceBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        MPlayer mplayer = MPlayer.get(player);
        if (mplayer == null) {
            return false;
        }

        MPerm buildPermission = MPerm.getPermBuild();
        PS psLocation = PS.valueOf(location);
        return buildPermission.has(mplayer, psLocation, false);
    }

    @Override
    public boolean canBreakBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        return canPlaceBlock(player, location);
    }

    @Override
    public @NotNull Set<UUID> getMembers() {
        Faction faction = getFaction();
        List<MPlayer> memberList = faction.getMPlayers();

        Set<UUID> memberIdSet = new HashSet<>();
        for (MPlayer member : memberList) {
            UUID memberId = member.getUuid();
            memberIdSet.add(memberId);
        }

        return Collections.unmodifiableSet(memberIdSet);
    }
}
