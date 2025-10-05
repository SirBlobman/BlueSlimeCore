package com.github.sirblobman.api.factions;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.github.sirblobman.api.utility.Validate;

import dev.kitteh.factions.FLocation;
import dev.kitteh.factions.FPlayer;
import dev.kitteh.factions.FPlayers;
import dev.kitteh.factions.Faction;
import dev.kitteh.factions.permissible.PermissibleActions;
import dev.kitteh.factions.permissible.Role;

public final class FactionWrapper_UUID4 extends FactionWrapper {
    private final Faction faction;

    public FactionWrapper_UUID4(Faction faction) {
        this.faction = Validate.notNull(faction, "faction must not be null!");
    }

    private @NotNull Faction getFaction() {
        return this.faction;
    }

    @Override
    public @NotNull String getFactionId() {
        Faction faction = getFaction();
        return Integer.toString(faction.id());
    }

    @Override
    public @NotNull String getFactionName() {
        Faction faction = getFaction();
        return faction.tag();
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
        List<FPlayer> fplayerList = faction.members(Role.ADMIN);
        if (fplayerList.isEmpty()) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        for (FPlayer fplayer : fplayerList) {
            UUID adminId = fplayer.uniqueId();
            if (playerId.equals(adminId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isMember(@NotNull OfflinePlayer player) {
        Faction faction = getFaction();
        Set<FPlayer> memberSet = faction.members();

        UUID playerId = player.getUniqueId();
        for (FPlayer member : memberSet) {
            UUID memberId = member.uniqueId();
            if (playerId.equals(memberId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canPlaceBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayers fplayers = FPlayers.fPlayers();
        FPlayer fplayer = fplayers.get(player);

        Faction faction = getFaction();
        FLocation flocation = new FLocation(location);
        return faction.hasAccess(fplayer, PermissibleActions.BUILD, flocation);
    }

    @Override
    public boolean canBreakBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayers fplayers = FPlayers.fPlayers();
        FPlayer fplayer = fplayers.get(player);

        Faction faction = getFaction();
        FLocation flocation = new FLocation(location);
        return faction.hasAccess(fplayer, PermissibleActions.DESTROY, flocation);
    }

    @Override
    public @NotNull Set<UUID> getMembers() {
        Faction faction = getFaction();
        Set<FPlayer> memberSet = faction.members();
        return memberSet.stream().map(FPlayer::uniqueId).collect(Collectors.toUnmodifiableSet());
    }
}
