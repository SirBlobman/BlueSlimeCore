package com.github.sirblobman.api.language.replacer;

import com.github.sirblobman.api.shaded.adventure.text.Component;

import org.jetbrains.annotations.NotNull;

public final class IntegerReplacer extends Replacer {
    private final int replacement;

    public IntegerReplacer(String target, int replacement) {
        super(target);
        this.replacement = replacement;
    }

    @Override
    public @NotNull Component getReplacement() {
        return Component.text(this.replacement);
    }

    @Override
    public @NotNull String getReplacementString() {
        return Integer.toString(this.replacement);
    }
}
