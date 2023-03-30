package com.github.sirblobman.api.language.replacer;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class StringReplacer extends Replacer {
    private final String replacement;

    public StringReplacer(String target, String replacement) {
        super(target);
        this.replacement = Validate.notNull(replacement, "replacement must not be null!");
    }

    @Override
    public @NotNull Component getReplacement() {
        return Component.text(this.replacement);
    }

    @Override
    public @NotNull String getReplacementString() {
        return this.replacement;
    }
}
