package com.github.sirblobman.api.factions;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.github.sirblobman.api.utility.Validate;

import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.PlayerManager;
import org.jetbrains.annotations.NotNull;

public final class FactionWrapper_X extends FactionWrapper {
    private final Faction faction;

    public FactionWrapper_X(Faction faction) {
        this.faction = Validate.notNull(faction, "faction must not be null!");
    }

    private @NotNull Faction getFaction() {
        return this.faction;
    }

    @Override
    public @NotNull String getFactionId() {
        Faction faction = getFaction();
        long factionId = faction.getId();
        return Long.toString(factionId);
    }

    @Override
    public @NotNull String getFactionName() {
        Faction faction = getFaction();
        return faction.getTag();
    }

    @Override
    public boolean isSafeZone() {
        Faction faction = getFaction();
        return faction.isSafezone();
    }

    @Override
    public boolean isWarZone() {
        Faction faction = getFaction();
        return faction.isWarzone();
    }

    @Override
    public boolean isWilderness() {
        Faction faction = getFaction();
        return faction.isWilderness();
    }

    @Override
    public boolean isLeader(@NotNull OfflinePlayer player) {
        Faction faction = getFaction();
        FPlayer leader = faction.getLeader();
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
        Set<UUID> memberIdSet = faction.getFactionMembers();

        UUID playerId = player.getUniqueId();
        return memberIdSet.contains(playerId);
    }

    @Override
    public boolean canPlaceBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if (factionPlayer == null) {
            return false;
        }

        return factionPlayer.canBuildAt(location);
    }

    @Override
    public boolean canBreakBlock(@NotNull OfflinePlayer player, @NotNull Location location) {
        UUID playerId = player.getUniqueId();
        FPlayer factionPlayer = PlayerManager.INSTANCE.getFPlayer(playerId);
        if (factionPlayer == null) {
            return false;
        }

        return factionPlayer.canBreakAt(location);
    }

    @Override
    public @NotNull Set<UUID> getMembers() {
        Faction faction = getFaction();
        return faction.getFactionMembers();
    }
}
