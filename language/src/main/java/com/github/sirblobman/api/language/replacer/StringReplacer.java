package com.github.sirblobman.api.language.replacer;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class StringReplacer extends Replacer {
    private final String replacement;

    public StringReplacer(@NotNull String target, @NotNull String replacement) {
        super(target);
        this.replacement = replacement;
    }

    @Override
    public @NotNull Component getReplacement() {
        String replacement = getReplacementString();
        return Component.text(replacement);
    }

    @Override
    public @NotNull String getReplacementString() {
        return this.replacement;
    }
}
