package com.github.sirblobman.api.language;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;

public final class Language {
    private final String languageName;
    private final LanguageConfiguration configuration;
    private final Locale javaLocale;

    public Language(@NotNull String languageName, @NotNull LanguageConfiguration configuration) {
        this.languageName = languageName;
        this.configuration = configuration;

        Locale javaLocale = Locale.forLanguageTag(languageName);
        if (javaLocale == null) {
            this.javaLocale = Locale.US;
        } else {
            this.javaLocale = javaLocale;
        }
    }

    public @NotNull String getLanguageName() {
        return this.languageName;
    }

    public @NotNull LanguageConfiguration getConfiguration() {
        return this.configuration;
    }

    public @NotNull Locale getJavaLocale() {
        return this.javaLocale;
    }
}
