package com.github.sirblobman.api.language;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.Nullable;

public final class LanguageReplacer implements Replacer {
    private final LanguageManager languageManager;
    private final String languageKey;
    private final String target;
    private final CommandSender audience;

    public LanguageReplacer(LanguageManager languageManager, CommandSender audience, String languageKey,
                            String target) {
        this.languageManager = Validate.notNull(languageManager, "languageManager must not be null!");
        this.languageKey = Validate.notEmpty(languageKey, "languageKey must not be empty!");
        this.target = Validate.notEmpty(target, "target must not be empty!");
        this.audience = audience;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public String getLanguageKey() {
        return this.languageKey;
    }

    public String getTarget() {
        return this.target;
    }

    @Nullable
    public CommandSender getAudience() {
        return this.audience;
    }

    @Override
    public String replace(String string) {
        CommandSender audience = getAudience();
        LanguageManager languageManager = getLanguageManager();
        String languageKey = getLanguageKey();

        String target = getTarget();
        String replacement = languageManager.getMessageString(audience, languageKey, null);
        return string.replace(target, replacement);
    }
}
