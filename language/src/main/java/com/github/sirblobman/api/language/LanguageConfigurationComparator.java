package com.github.sirblobman.api.language;

import java.util.Comparator;

import org.bukkit.configuration.ConfigurationSection;

import com.github.sirblobman.api.utility.Validate;

public class LanguageConfigurationComparator implements Comparator<ConfigurationSection> {
    @Override
    public int compare(ConfigurationSection section1, ConfigurationSection section2) {
        Validate.notNull(section1, "section1 must not be null!");
        Validate.notNull(section2, "section2 must not be null!");

        String languageName1 = section1.getString("language-name");
        String languageName2 = section2.getString("language-name");
        if(languageName1 == null || languageName2 == null) {
            throw new IllegalStateException("A file is missing a language name!");
        }

        String section1Parent = section1.getString("parent");
        String section2Parent = section2.getString("parent");
        if(section1Parent == null && section2Parent == null) {
            return languageName1.compareToIgnoreCase(languageName2);
        }

        if(languageName1.equals(section1Parent) || languageName2.equals(section2Parent)) {
            throw new IllegalStateException("A language can't depend on itself!");
        }

        if(languageName2.equals(section1Parent) && languageName1.equals(section2Parent)) {
            throw new IllegalStateException("Cyclic language dependency!");
        }

        if(languageName1.equals(section2Parent)) return 1;
        if(languageName2.equals(section1Parent)) return -1;
        return 0;
    }
}
