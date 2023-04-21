package com.github.sirblobman.api.configuration;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigurable {
    /**
     * Load the values from a configuration.
     *
     * @param section The configuration that contains the values.
     */
    void load(@NotNull ConfigurationSection section);

    /**
     * Save the values to a configuration.
     *
     * @param section The configuration that wil contain the values.
     */
    default void save(@NotNull ConfigurationSection section) {
        // Do Nothing
    }

    /**
     * Get or create a configuration section.
     *
     * @param parent The parent section.
     * @param path   The path to check.
     * @return The original section at the path, or a new one if a section did not exist.
     */
    default ConfigurationSection getOrCreateSection(@NotNull ConfigurationSection parent, @NotNull String path) {
        ConfigurationSection oldSection = parent.getConfigurationSection(path);
        if (oldSection != null) {
            return oldSection;
        }

        return parent.createSection(path);
    }
}
