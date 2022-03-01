package com.github.sirblobman.api.language;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Language {
    private final String languageCode;
    private final Language parentLanguage;
    private final Map<String, String> translationMap;
    
    /**
     * Language Constructor
     *
     * @param parentLanguage The language that this language inherits from.
     * @param languageCode   The code ID for this language (Example: "en_us")
     */
    public Language(@Nullable Language parentLanguage, @NotNull String languageCode) {
        this.parentLanguage = parentLanguage;
        this.languageCode = Validate.notNull(languageCode, "languageCode must not be null!");
        this.translationMap = new HashMap<>();
    }
    
    /**
     * Language Constructor
     *
     * @param languageCode The code ID for this language (Example: "en_us")
     */
    public Language(@NotNull String languageCode) {
        this(null, languageCode);
    }
    
    @NotNull
    public Optional<Language> getParent() {
        return Optional.ofNullable(this.parentLanguage);
    }
    
    @NotNull
    public String getLanguageCode() {
        return this.languageCode;
    }
    
    public Optional<Locale> getJavaLocale() {
        String languageCode = getLanguageCode();
        Locale locale = Locale.forLanguageTag(languageCode);
        return Optional.ofNullable(locale);
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
        Optional<Language> parentLanguageOptional = getParent();
        String translation = this.translationMap.getOrDefault(key, "");
        if(parentLanguageOptional.isPresent()) {
            Language parentLanguage = parentLanguageOptional.get();
            return parentLanguage.getTranslation(key);
        }
        
        return translation;
    }
    
    protected void addTranslation(@NotNull String key, @NotNull String value) {
        Validate.notEmpty(key, "key must not be empty or null!");
        Validate.notNull(value, "value must not be null!");
        this.translationMap.put(key, value);
    }
}
