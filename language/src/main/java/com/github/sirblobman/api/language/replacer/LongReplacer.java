package com.github.sirblobman.api.language.replacer;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class LongReplacer extends Replacer {
    private final long replacement;

    public LongReplacer(String target, long replacement) {
        super(target);
        this.replacement = replacement;
    }

    private long getLong() {
        return this.replacement;
    }

    @Override
    public @NotNull Component getReplacement() {
        long replacement = getLong();
        return Component.text(replacement);
    }

    @Override
    public @NotNull String getReplacementString() {
        long replacement = getLong();
        return Long.toString(replacement);
    }
}
