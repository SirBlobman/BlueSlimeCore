package com.github.sirblobman.api.language.replacer;

import com.github.sirblobman.api.shaded.adventure.text.Component;

import org.jetbrains.annotations.NotNull;

public final class LongReplacer extends Replacer {
    private final long replacement;

    public LongReplacer(String target, long replacement) {
        super(target);
        this.replacement = replacement;
    }

    @Override
    public @NotNull Component getReplacement() {
        return Component.text(this.replacement);
    }

    @Override
    public @NotNull String getReplacementString() {
        return Long.toString(this.replacement);
    }
}
