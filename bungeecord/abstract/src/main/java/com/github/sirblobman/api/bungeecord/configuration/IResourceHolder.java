package com.github.sirblobman.api.bungeecord.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Used to indicate an object that can store/save files and log information.
 */
public interface IResourceHolder {
    File getDataFolder();
    
    InputStream getResource(String name);
    
    Logger getLogger();
}
