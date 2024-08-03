package com.github.sirblobman.api.factions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.github.sirblobman.api.utility.Validate;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;

public final class FactionWrapper_UUID_Legacy extends FactionWrapper {
    private final Faction faction;

    public FactionWrapper_UUID_Legacy(Faction faction) {
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
        return faction.getTag();
    }

    @Override
    public boolean isSafeZone() {
        Faction faction = getFaction();
        return faction.isSafeZone();
    }

    @Override
    public boolean isWarZone() {
        Faction faction = getFaction();
        return faction.isWarZone();
    }

    @Override
    public boolean isWilderness() {
        Faction faction = getFaction();
        return faction.isWilderness();
    }

    @Override
    public boolean isLeader(@NotNull OfflinePlayer player) {
        Faction faction = getFaction();
        List<FPlayer> fplayerList = faction.getFPlayersWhereRole(Role.ADMIN);
        if (fplayerList.isEmpty()) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        for (FPlayer fplayer : fplayerList) {
            String adminIdString = fplayer.getId();
            UUID adminId = UUID.fromString(adminIdString);
            if (playerId.equals(adminId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isMember(@NotNull OfflinePlayer player) {
        Faction faction = getFaction();
        Set<FPlayer> memberSet = faction.getFPlayers();

        UUID playerId = player.getUniqueId();
        for (FPlayer member : memberSet) {
            String memberIdString = member.getId();
            UUID memberId = UUID.fromString(memberIdString);
            if (playerId.equals(memberId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canPlaceBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        if (fplayer == null) {
            return false;
        }

        Faction faction = getFaction();
        Access access = faction.getAccess(fplayer, PermissableAction.BUILD);
        return (access == Access.ALLOW);
    }

    @Override
    public boolean canBreakBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayers fplayers = FPlayers.getInstance();
        FPlayer fplayer = fplayers.getByOfflinePlayer(player);
        if (fplayer == null) {
            return false;
        }

        Faction faction = getFaction();
        Access access = faction.getAccess(fplayer, PermissableAction.DESTROY);
        return (access == Access.ALLOW);
    }

    @Override
    public @NotNull Set<UUID> getMembers() {
        Faction faction = getFaction();
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
