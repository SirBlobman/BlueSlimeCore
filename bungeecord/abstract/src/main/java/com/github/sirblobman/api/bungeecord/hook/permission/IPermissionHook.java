package com.github.sirblobman.api.bungeecord.hook.permission;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.sirblobman.api.bungeecord.hook.IHook;

public interface IPermissionHook extends IHook {
    /**
     * Get the prefix text for a specific player.
     *
     * @param playerId The {@link UUID} of the player.
     * @return The prefix for the player, or an empty string if they do not have one.
     */
    @Nullable String getPrefix(@NotNull UUID playerId);

    /**
     * Get the suffix text for a specific player.
     *
     * @param playerId The {@link UUID} of the player.
     * @return The suffix for the player, or an empty string if they do not have one.
     */
    @Nullable String getSuffix(@NotNull UUID playerId);

    /**
     * Get the current primary group name for a player.
     *
     * @param playerId The {@link UUID} of the player.
     * @return The primary group name for the player, or an empty string if they do not have one.
     */
    @Nullable String getPrimaryGroupName(@NotNull UUID playerId);

    /**
     * Get the current LuckPerms primary group weight for a player.
     *
     * @param playerId      The {@link UUID} of the player.
     * @param defaultWeight The default weight to use if the player doesn't have a group or weight.
     * @return The weight for the player's primary group, or the value of {@code defaultWeight}.
     */
    int getPrimaryGroupWeight(@NotNull UUID playerId, int defaultWeight);
}
