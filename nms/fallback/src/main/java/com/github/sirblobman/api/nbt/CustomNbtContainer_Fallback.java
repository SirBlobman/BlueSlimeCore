package com.github.sirblobman.api.nbt;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.plugin.Plugin;

public final class CustomNbtContainer_Fallback implements CustomNbtContainer {
    private final Plugin plugin;

    public CustomNbtContainer_Fallback(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public <T, Z> void set(@NotNull String key, @NotNull CustomNbtType<T, Z> type, @NotNull Z value) {
        // Do Nothing
    }

    @Override
    public <T, Z> boolean has(@NotNull String key, @NotNull CustomNbtType<T, Z> type) {
        return false;
    }

    @Override
    public <T, Z> @Nullable Z get(@NotNull String key, @NotNull CustomNbtType<T, Z> type) {
        return null;
    }

    @Override
    public <T, Z> @Nullable Z getOrDefault(@NotNull String key, @NotNull CustomNbtType<T, Z> type,
                                           @Nullable Z defaultValue) {
        return defaultValue;
    }

    @Override
    public @NotNull Set<String> getKeys() {
        return Collections.emptySet();
    }

    @Override
    public void remove(@NotNull String key) {
        // Do Nothing
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull CustomNbtContext getContext() {
        Plugin plugin = getPlugin();
        return new CustomNbtContext_Fallback(plugin);
    }
}
