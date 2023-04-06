package com.github.sirblobman.api.factions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.github.sirblobman.api.utility.Validate;

import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.locality.Locality;
import net.redstoneore.legacyfactions.locality.LocalityOwnership;
import org.jetbrains.annotations.NotNull;

public final class FactionWrapper_Legacy extends FactionWrapper {
    private final Faction faction;

    public FactionWrapper_Legacy(Faction faction) {
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
        FPlayer owner = faction.getOwner();
        if (owner == null) {
            return false;
        }

        UUID playerId = player.getUniqueId();
        String ownerId = owner.getId();
        return playerId.equals(UUID.fromString(ownerId));
    }

    @Override
    public boolean isMember(@NotNull OfflinePlayer player) {
        Set<UUID> memberSet = getMembers();
        UUID playerId = player.getUniqueId();
        return memberSet.contains(playerId);
    }

    @Override
    public boolean canPlaceBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayer fplayer = FPlayerColl.get(player);
        if (fplayer == null) {
            return false;
        }

        Locality locality = Locality.of(location);
        LocalityOwnership ownership = locality.getOwnership();
        return (ownership != null && ownership.hasAccess(fplayer));
    }

    @Override
    public boolean canBreakBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        FPlayer fplayer = FPlayerColl.get(player);
        if (fplayer == null) {
            return false;
        }

        Locality locality = Locality.of(location);
        LocalityOwnership ownership = locality.getOwnership();
        return (ownership != null && ownership.hasAccess(fplayer));
    }

    @Override
    public @NotNull Set<UUID> getMembers() {
        Faction faction = getFaction();
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
