package com.github.sirblobman.api.language;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.Nullable;

public final class DecimalReplacer implements Replacer {
    private final LanguageManager languageManager;
    private final CommandSender audience;
    private final String target;
    private final Number number;

    public DecimalReplacer(LanguageManager languageManager, CommandSender audience, String target,
                           Number number) {
        this.languageManager = Validate.notNull(languageManager, "languageManager must not be null!");
        this.target = Validate.notEmpty(target, "target must not be empty!");
        this.number = Validate.notNull(number, "number must not be null!");
        this.audience = audience;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public String getTarget() {
        return this.target;
    }

    public Number getNumber() {
        return this.number;
    }

    @Nullable
    public CommandSender getAudience() {
        return this.audience;
    }

    @Override
    public String replace(String string) {
        CommandSender audience = getAudience();
        LanguageManager languageManager = getLanguageManager();

        String target = getTarget();
        Number number = getNumber();
        String replacement = languageManager.formatDecimal(audience, number);
        return string.replace(target, replacement);
    }
}
