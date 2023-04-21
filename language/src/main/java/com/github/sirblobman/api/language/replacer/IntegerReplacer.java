package com.github.sirblobman.api.language.replacer;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class IntegerReplacer extends Replacer {
    private final int replacement;

    public IntegerReplacer(@NotNull String target, int replacement) {
        super(target);
        this.replacement = replacement;
    }

    private int getInteger() {
        return this.replacement;
    }

    @Override
    public @NotNull Component getReplacement() {
        int replacement = getInteger();
        return Component.text(replacement);
    }

    @Override
    public @NotNull String getReplacementString() {
        int replacement = getInteger();
        return Integer.toString(replacement);
    }
}
