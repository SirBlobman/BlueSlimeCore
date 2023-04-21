package com.github.sirblobman.api.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.utility.Validate;

public final class ConfigurationManager {
    private final File baseFolder;
    private final IResourceHolder resourceHolder;
    private final Map<String, YamlConfiguration> configurationMap;

    /**
     * A configuration manager that use the {@link Plugin#getDataFolder()} as the base folder.
     *
     * @param plugin The plugin being used.
     */
    public ConfigurationManager(@NotNull Plugin plugin) {
        this(new WrapperPluginResourceHolder(plugin));
    }

    public ConfigurationManager(@NotNull IResourceHolder resourceHolder) {
        this.resourceHolder = resourceHolder;
        this.baseFolder = resourceHolder.getDataFolder();
        this.configurationMap = new HashMap<>();
    }

    /**
     * @return The {@link IResourceHolder} managing these configuration files.
     */
    public @NotNull IResourceHolder getResourceHolder() {
        return this.resourceHolder;
    }

    /**
     * @return The base directory that all files will be contained in.
     */
    public @NotNull File getBaseFolder() {
        return this.baseFolder;
    }

    /**
     * Copies the default configuration from the jar if it does not already exist.
     *
     * @param fileName The relative name of the configuration to copy
     */
    public void saveDefault(@NotNull String fileName) {
        File file = getFile(fileName);
        saveDefault(fileName, file);
    }

    /**
     * @param fileName The name of the internal file.
     * @return A configuration stored inside the resource holder
     * ({@code null} if the file does not exist or an error occurred.)
     * @see IResourceHolder#getResource(String)
     */
    public @Nullable YamlConfiguration getInternal(@NotNull String fileName) {
        IResourceHolder resourceHolder = getResourceHolder();
        InputStream inputStream = resourceHolder.getResource(fileName);
        if (inputStream == null) {
            return null;
        }

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.load(inputStreamReader);
            return configuration;
        } catch (IOException | InvalidConfigurationException ex) {
            Logger logger = resourceHolder.getLogger();
            logger.log(Level.WARNING, "Failed to load internal configuration from classpath:", ex);
            return null;
        }
    }

    /**
     * @param fileName The relative name of the configuration to get
     * @return A configuration from memory. If the configuration is not in memory it will be loaded from storage first.
     * If a file can't be loaded, an empty configuration will be returned.
     */
    public @NotNull YamlConfiguration get(@NotNull String fileName) {
        YamlConfiguration configuration = this.configurationMap.getOrDefault(fileName, null);
        if (configuration != null) {
            return configuration;
        }

        reload(fileName);
        return this.configurationMap.computeIfAbsent(fileName, key -> new YamlConfiguration());
    }

    /**
     * Save a configuration from memory to storage.
     *
     * @param fileName The relative name of the configuration.
     */
    public void save(@NotNull String fileName) {
        try {
            YamlConfiguration configuration = this.configurationMap.getOrDefault(fileName, null);
            if (configuration == null) {
                return;
            }

            File file = getFile(fileName);
            configuration.save(file);
        } catch (IOException ex) {
            Logger logger = getResourceHolder().getLogger();
            logger.log(Level.WARNING, "An I/O exception occurred while saving a configuration file:", ex);
        }
    }

    /**
     * Load a configuration from storage into memory.
     *
     * @param fileName The relative name of the configuration.
     */
    public void reload(@NotNull String fileName) {
        File file = getFile(fileName);
        IResourceHolder resourceHolder = getResourceHolder();
        if (!file.exists() || !file.isFile()) {
            Logger logger = resourceHolder.getLogger();
            logger.warning("'" + fileName + "' could not be reloaded because it is not a file or does not exist!");
            return;
        }

        try {
            YamlConfiguration configuration = new YamlConfiguration();

            YamlConfiguration jarConfiguration = getInternal(fileName);
            if (jarConfiguration != null) {
                configuration.setDefaults(jarConfiguration);
            }

            this.configurationMap.put(fileName, configuration);
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger logger = resourceHolder.getLogger();
            logger.log(Level.WARNING, "An I/O exception occurred while loading a configuration file:", ex);
            logger.log(Level.WARNING, "Using default configuration from jar file instead.");
        }
    }

    private File getFile(@NotNull String fileName) {
        Validate.notEmpty(fileName, "fileName must not be empty!");
        File baseFolder = getBaseFolder();
        return new File(baseFolder, fileName);
    }

    private void saveDefault(@NotNull String fileName, @NotNull File realFile) {
        Validate.notEmpty(fileName, "jarName must not be empty!");
        Validate.notNull(realFile, "realFile cannot be null!");
        if (realFile.exists()) {
            return;
        }

        IResourceHolder resourceHolder = getResourceHolder();
        InputStream jarStream = resourceHolder.getResource(fileName);
        if (jarStream == null) {
            Logger logger = resourceHolder.getLogger();
            logger.warning("Failed to save default config '" + fileName + "' because it does not exist in the jar.");
            return;
        }

        try {
            File parentFile = realFile.getParentFile();
            if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
                Logger logger = resourceHolder.getLogger();
                logger.warning("Failed to save default config '" + fileName + "' because the parent folder could not be created.");
                return;
            }

            if (!realFile.createNewFile()) {
                Logger logger = resourceHolder.getLogger();
                logger.warning("Failed to save default config '" + fileName + "' because the file could not be created.");
                return;
            }

            Path absolutePath = realFile.toPath();
            Files.copy(jarStream, absolutePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger logger = resourceHolder.getLogger();
            logger.log(Level.WARNING, "An I/O exception occurred while saving a default file:", ex);
        }
    }
}
