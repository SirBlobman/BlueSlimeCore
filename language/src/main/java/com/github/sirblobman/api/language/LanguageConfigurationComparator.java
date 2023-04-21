package com.github.sirblobman.api.language;

import java.util.Comparator;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

public final class LanguageConfigurationComparator implements Comparator<ConfigurationSection> {
    @Override
    public int compare(@NotNull ConfigurationSection section1, @NotNull ConfigurationSection section2) {
        String languageName1 = section1.getString("language-name");
        String languageName2 = section2.getString("language-name");
        String parentName1 = section1.getString("parent");
        String parentName2 = section2.getString("parent");

        if (Objects.equals(parentName2, languageName2) && Objects.equals(parentName2, languageName1)) {
            throw new IllegalStateException("Cyclic Language Dependency: " + languageName1 + ", " + languageName2);
        }

        if (Objects.equals(parentName1, languageName2)) {
            return 1;
        }

        if (Objects.equals(parentName2, languageName1)) {
            return -1;
        }

        return languageName1.compareToIgnoreCase(languageName2);
    }
}
