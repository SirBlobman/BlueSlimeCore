package com.github.sirblobman.api.bungeecord.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

/**
 * Used to indicate an object that can store/save files and log information.
 */
public interface IResourceHolder {
    @NotNull File getDataFolder();

    @NotNull InputStream getResource(String name);

    @NotNull Logger getLogger();
}
