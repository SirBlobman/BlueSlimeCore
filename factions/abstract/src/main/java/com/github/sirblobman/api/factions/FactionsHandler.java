package com.github.sirblobman.api.factions;

import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

public abstract class FactionsHandler {
    private final JavaPlugin plugin;

    public FactionsHandler(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    public Object getFactionAt(Entity entity) {
        Location location = entity.getLocation();
        return getFactionAt(location);
    }

    public abstract Object getFactionAt(Location location);

    public abstract String getFactionNameAt(Location location);

    public abstract boolean isSafeZone(Location location);

    public abstract boolean isWarZone(Location location);

    public abstract boolean isWilderness(Location location);

    public abstract boolean hasFaction(OfflinePlayer player);

    public abstract Object getFactionFor(OfflinePlayer player);

    public abstract String getFactionNameFor(OfflinePlayer player);

    /**
     * @param player1 The first player
     * @param player2 The second player
     * @return {@code true} if player2 is the same as player1. {@code false} if either player is not in a faction.
     * {@code true} if player2 has an ally relationship with player1. {@code true} if player2 is in the same faction as
     * player1. {@code false} otherwise
     */
    public abstract boolean isMemberOrAlly(OfflinePlayer player1, OfflinePlayer player2);

    /**
     * @param player1 The first player
     * @param player2 The second player
     * @return {@code false} if player2 is the same as player1. {@code false} if either player is not in a faction.
     * {@code true} if player2 has an enemy relationship with player1. {@code false} otherwise
     */
    public abstract boolean isEnemy(OfflinePlayer player1, OfflinePlayer player2);

    /**
     * @param player The player to check
     * @return {@code true} if the player has admin bypass mode enabled, {@code false} otherwise.
     */
    public abstract boolean hasBypass(OfflinePlayer player);

    public abstract boolean isInEnemyLand(OfflinePlayer player, Location location);

    public abstract boolean isInOwnFaction(OfflinePlayer player, Location location);

    public abstract boolean isLeader(OfflinePlayer player, Location location);

    public abstract boolean canBuild(OfflinePlayer player, Location location);

    public abstract boolean canDestroy(OfflinePlayer player, Location location);

    public abstract ChatColor getRelationChatColor(OfflinePlayer viewer, OfflinePlayer player);

    public abstract String getRolePrefix(OfflinePlayer player);

    public abstract Set<UUID> getMembersForFactionAt(Location location);

    public abstract Set<UUID> getMembersForFactionOf(OfflinePlayer player);
}
