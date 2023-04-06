package com.github.sirblobman.api.factions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class to handle methods that are required by plugins that work with Factions
 * @author SirBlobman
 */
public abstract class FactionsHandler {
    /**
     * Check if a player is a member of a faction.
     * System factions such as "wilderness", "safe zone", and "war zone" should return {@code false}
     *
     * @param player The player that will be checked.
     * @return {@code true} if the player is a member of a Faction.
     * @throws NullPointerException if the {@code player} parameter is null.
     */
    public abstract boolean hasFaction(@NotNull OfflinePlayer player);

    /**
     * @param player The player that will be checked.
     * @return a wrapped object representing the Faction that the player is a member of.
     * @throws NullPointerException if the {@code player} parameter is null.
     */
    public abstract @Nullable FactionWrapper getFactionFor(@NotNull OfflinePlayer player);

    /**
     * @param location The location to check.
     * @return a wrapped object representing the Faction at the specified Location,
     * or null if there is no Faction at that location.
     * @throws NullPointerException if the {@code location} parameter is null.
     */
    public abstract @Nullable FactionWrapper getFactionAt(@NotNull Location location);

    /**
     * @param entity The entity to check.
     * @return a wrapped object representing the Faction at the location of the specified Entity,
     * or null if there is no Faction at that location.
     * @throws NullPointerException if the {@code entity} parameter is null.
     */
    public @Nullable FactionWrapper getFactionAt(@NotNull Entity entity) {
        Location location = entity.getLocation();
        return getFactionAt(location);
    }

    /**
     * Determines if the second player is in an ALLY relationship with the first player.
     * Players who are members of the same faction are considered allies,
     * and a player is always considered an ally to themselves.
     *
     * @param player1 The first player.
     * @param player2 The second player.
     * @return {@code true} if the second player is considered an ally of the first, otherwise {@code false}.
     * @throws NullPointerException if either player parameter is null.
     */
    public abstract boolean isAlly(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2);

    /**
     * Determines if the second player is in an ENEMY relationship with the first player.
     * Players who are members of the same faction are not considered enemies,
     * and a player is never considered an enemy to themselves.
     *
     * @param player1 The first player.
     * @param player2 The second player.
     * @return {@code true} if the second player is considered an enemy of the first, otherwise {@code false}.
     * @throws NullPointerException if either player parameter is null.
     */
    public abstract boolean isEnemy(@NotNull OfflinePlayer player1, @NotNull OfflinePlayer player2);

    /**
     * Determines if the player has admin bypass permissions (also known as admin override).
     * @param player The player that will be checked.
     * @return {@code true} if the player has admin bypass, otherwise {@code false}.
     */
    public abstract boolean hasBypass(@NotNull OfflinePlayer player);


    /**
     * Determines if the specified Location is considered enemy territory for the given player.
     * A Location is considered enemy territory if it is not owned by the player's Faction or an ally Faction.
     * @param player the player to check for enemy territory, must not be null
     * @param location the Location to check, must not be null
     * @return {@code true} if the Location is considered enemy territory for the player, otherwise {@code false}
     * @throws NullPointerException if either the player or location parameter is null
     * @see #getFactionAt(Location)
     */
    public abstract boolean isInEnemyLand(@NotNull OfflinePlayer player, @NotNull Location location);

    /**
     * Determines if the specified Location is owned by the player's Faction.
     * @param player the player to check, must not be null
     * @param location the Location to check, must not be null
     * @return {@code true} if the Location owned by the player's faction, otherwise {@code false}
     * @throws NullPointerException if either the player or location parameter is null
     * @see #getFactionAt(Location)
     */
    public abstract boolean isInOwnFaction(@NotNull OfflinePlayer player, @NotNull Location location);

    /**
     * Determines the {@link ChatColor} for the Faction relationship between the viewer and the player.
     * The colors used depend on the Faction plugin, but usually match the following:<br/>
     * &bull; Enemy: {@link ChatColor#RED}<br/>
     * &bull; Ally: {@link ChatColor#LIGHT_PURPLE}<br/>
     * &bull; Neutral: {@link ChatColor#WHITE}<br/>
     * If the Faction plugin doesn't have a color for a relationship, {@code null} may be returned.
     * @param viewer the player who will see the chat color, must not be null
     * @param player the player whose relationship is being viewed, must not be null
     * @return the ChatColor corresponding to the relationship between the viewer and the player,
     * or {@code null} if the relationship is unknown
     * @throws NullPointerException if either the viewer or player parameter is null
     */
    public abstract @Nullable ChatColor getRelationChatColor(@NotNull OfflinePlayer viewer,
                                                             @NotNull OfflinePlayer player);

    /**
     * Determines the role prefix for a player. Most plugins match the following:<br/>
     * &bull; Leader: **<br/>
     * &bull; Member: *<br/>
     * &bull; Recruit: no prefix<br/>
     * If the Faction plugin doesn't have role prefixes, {@code null} may be returned.
     * @param player the player to check.
     * @return The prefix corresponding to the role of the player,
     * or {@code null} if the Faction plugin doesn't have a prefix.
     * @throws NullPointerException if the player parameter is null
     */
    public abstract @Nullable String getRolePrefix(@NotNull OfflinePlayer player);
}
