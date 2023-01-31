package com.github.sirblobman.api.language;

import java.util.Locale;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class Language {
    private final String languageName;
    private final LanguageConfiguration configuration;
    private final Locale javaLocale;

    public Language(String languageName, LanguageConfiguration configuration) {
        this.languageName = Validate.notEmpty(languageName, "languageName must not be empty!");
        this.configuration = Validate.notNull(configuration, "configuration must not be null!");

        Locale javaLocale = Locale.forLanguageTag(languageName);
        if (javaLocale == null) {
            this.javaLocale = Locale.US;
        } else {
            this.javaLocale = javaLocale;
        }
    }

    @NotNull
    public String getLanguageName() {
        return this.languageName;
    }

    @NotNull
    public LanguageConfiguration getConfiguration() {
        return this.configuration;
    }

    @NotNull
    public Locale getJavaLocale() {
        return this.javaLocale;
    }
}
