package com.github.sirblobman.api.factions;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import org.jetbrains.annotations.NotNull;

/**
 * A class that allows accessing values from an instance of a Faction.
 * @author SirBlobman
 */
public abstract class FactionWrapper {
    /**
     * @return The ID for comparing two different instances of a Faction.
     */
    public abstract @NotNull String getFactionId();

    /**
     * @return The name of the Faction that will be displayed to players.
     */
    public abstract @NotNull String getFactionName();

    /**
     * Returns whether the Faction is considered a safe zone,
     * meaning that PvP (Player versus Player) is not allowed within it.
     * @return {@code true} if the Faction is a safe zone, otherwise {@code false}.
     */
    public abstract boolean isSafeZone();

    /**
     * Returns whether the Faction is considered a war zone,
     * meaning that PvP is always allowed within it.
     * @return {@code true} if the Faction is a war zone, otherwise {@code false}.
     */
    public abstract boolean isWarZone();

    /**
     * Returns whether the Faction is considered part of the wilderness,
     * meaning that it is not owned by any player or Faction.
     * @return {@code true} if the Faction is wilderness, otherwise {@code false}.
     */
    public abstract boolean isWilderness();

    /**
     * Returns whether the specified player is a leader/administrator of this faction.
     * @param player The player to check.
     * @return {@code true} if the player is a leader or administrator, otherwise {@code false}.
     */
    public abstract boolean isLeader(@NotNull OfflinePlayer player);

    /**
     * Returns whether the specified player belongs to this faction.
     * @param player The player to check.
     * @return {@code true} if the player is a member, otherwise {@code false}.
     */
    public abstract boolean isMember(@NotNull OfflinePlayer player);

    /**
     * Determines whether the specified player has permission to place a block at the specified location within
     * the Faction.
     * @param player the player attempting to place the block, must not be null.
     * @param location the location where the block is to be placed, must not be null.
     * @return {@code true} if the player has permission to place a block at the location, otherwise {@code} false.
     * @throws NullPointerException if either the player or location parameter is null
     */
    public abstract boolean canPlaceBlock(@NotNull OfflinePlayer player, @NotNull Location location);

    /**
     * Determines whether the specified player has permission to break a block at the specified location within
     * the Faction.
     * @param player the player attempting to break the block, must not be null.
     * @param location  the location where the block would be broken, must not be null.
     * @return {@code true} if the player has permission to break a block at the location, otherwise {@code} false.
     * @throws NullPointerException if either the player or location parameter is null
     */
    public abstract boolean canBreakBlock(@NotNull OfflinePlayer player, @NotNull Location location);

    /**
     * Returns a set of UUIDs representing the players who belong to this Faction.
     * @return a non-null set of UUIDs representing the Faction members. Can be empty.
     */
    public abstract @NotNull Set<UUID> getMembers();
}
