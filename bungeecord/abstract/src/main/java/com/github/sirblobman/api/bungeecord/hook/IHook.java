package com.github.sirblobman.api.bungeecord.hook;

import net.md_5.bungee.api.plugin.Plugin;

public interface IHook {
    /**
     * @return The plugin that will be using the hook.
     */
    Plugin getPlugin();

    /**
     * Check if the plugin being hooked is disabled or does not exist.
     *
     * @return {@code true} if the hook doesn't exist on the proxy, otherwise {@code false}.
     */
    boolean isDisabled();

    default boolean hasListener() {
        return false;
    }

    default void registerListener() {
        // Do Nothing
    }
}
