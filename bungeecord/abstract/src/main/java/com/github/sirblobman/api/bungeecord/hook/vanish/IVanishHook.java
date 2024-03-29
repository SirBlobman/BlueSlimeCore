package com.github.sirblobman.api.bungeecord.hook.vanish;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.github.sirblobman.api.bungeecord.hook.IHook;

public interface IVanishHook extends IHook {
    /**
     * Check if a player is currently vanished.
     *
     * @param player The {@link ProxiedPlayer} to check.
     * @return {@code true} if the player is vanished, otherwise {@code false}.
     */
    boolean isHidden(@NotNull ProxiedPlayer player);

    /**
     * Check if a player is currently vanished.
     *
     * @param playerId The {@link UUID} of the player to check.
     * @return {@code true} if the player is vanished, otherwise {@code false}.
     */
    boolean isHidden(@NotNull UUID playerId);

    /**
     * Set the hide value of a player.
     *
     * @param player The {@link ProxiedPlayer} to hide/unhide.
     * @param hidden {@code true} to hide the player, {@code false} to unhide the player.
     */
    void setHidden(@NotNull ProxiedPlayer player, boolean hidden);

    /**
     * Set the hide value of a player.
     *
     * @param playerId The {@link UUID} of the player to hide/unhide.
     * @param hidden   {@code true} to hide the player, {@code false} to unhide the player.
     */
    void setHidden(@NotNull UUID playerId, boolean hidden);
}
