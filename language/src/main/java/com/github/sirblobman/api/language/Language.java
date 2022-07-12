package com.github.sirblobman.api.language;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Language {
    private final String languageCode;
    private final Language parentLanguage;
    private final Map<String, String> translationMap;
    private final YamlConfiguration originalConfiguration;

    private DecimalFormat decimalFormat;
    
    /**
     * Language Constructor
     *
     * @param parentLanguage The language that this language inherits from.
     * @param languageCode   The code ID for this language (Example: "en_us")
     * @param originalConfiguration The original {@link YamlConfiguration} for this language.
     */
    public Language(@Nullable Language parentLanguage, @NotNull String languageCode,
                    @NotNull YamlConfiguration originalConfiguration) {
        this.languageCode = Validate.notNull(languageCode, "languageCode must not be null!");
        this.originalConfiguration = Validate.notNull(originalConfiguration,
                "originalConfiguration must not be null!");
        this.parentLanguage = parentLanguage;
        this.translationMap = new HashMap<>();
    }
    
    @NotNull
    public Optional<Language> getParent() {
        return Optional.ofNullable(this.parentLanguage);
    }
    
    @NotNull
    public String getLanguageCode() {
        return this.languageCode;
    }

    @NotNull
    public YamlConfiguration getOriginalConfiguration() {
        return this.originalConfiguration;
    }

    @NotNull
    public Optional<Locale> getJavaLocale() {
        String languageCode = getLanguageCode();
        Locale locale = Locale.forLanguageTag(languageCode);
        return Optional.ofNullable(locale);
    }

    @NotNull
    public DecimalFormat getDecimalFormat() {
        if(this.decimalFormat != null) {
            return this.decimalFormat;
        }

        Locale javaLocale = getJavaLocale().orElse(Locale.US);

        String translation = getTranslation("decimal-format");
        if(translation.isEmpty()) {
            translation = "0.00";
        }

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(javaLocale);
        this.decimalFormat = new DecimalFormat(translation, decimalFormatSymbols);
        return this.decimalFormat;
    }
    
    /**
     * Get a message from a key based on this language.
     *
     * @param key The path to the message.
     * @return An empty string if the key doesn't exist. A translated message if one exists in this language or its
     * parent.
     * @throws StackOverflowError when there is a cyclic language dependency.
     */
    @NotNull
    public String getTranslation(String key) {
        String translation = this.translationMap.get(key);
        if(translation != null) {
            return translation;
        }

        Optional<Language> parentLanguageOptional = getParent();
        if(parentLanguageOptional.isPresent()) {
            Language language = parentLanguageOptional.get();
            return language.getTranslation(key);
        }

        return key;
    }
    
    void addTranslation(@NotNull String key, @NotNull String value) {
        Validate.notEmpty(key, "key must not be empty or null!");
        Validate.notNull(value, "value must not be null!");
        this.translationMap.put(key, value);
    }
}
