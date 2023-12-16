package com.github.sirblobman.api.bungeecord.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Used to indicate an object that can store/save files and log information.
 */
public interface IResourceHolder {
    @NotNull File getDataFolder();

    @Nullable InputStream getResource(String name);

    @NotNull Logger getLogger();
}
