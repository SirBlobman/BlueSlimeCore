package com.github.sirblobman.api.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Used to indicate an object that can store/save files and log information.
 */
public interface IResourceHolder {
    /**
     * @return The name of this resource holder.
     * Must not have any spaces.
     */
    @NotNull String getName();

    /**
     * @return A string for creating Minecraft key objects.
     * Must be lowercase without spaces or special symbols, but can contain "_"
     */
    @NotNull String getKeyName();

    @NotNull File getDataFolder();

    @Nullable InputStream getResource(@NotNull String name);

    @NotNull Logger getLogger();
}
