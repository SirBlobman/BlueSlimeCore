package com.github.sirblobman.api.language.replacer;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.TextReplacementConfig;

public abstract class Replacer {
    private final String target;

    public Replacer(@NotNull String target) {
        this.target = target;
    }

    public @NotNull String getTarget() {
        return this.target;
    }

    public abstract @NotNull Component getReplacement();

    public abstract @NotNull String getReplacementString();

    public final @NotNull String replaceString(@NotNull String original) {
        String target = getTarget();
        String replacement = getReplacementString();
        return original.replace(target, replacement);
    }

    public final @NotNull TextReplacementConfig asReplacementConfig() {
        String target = getTarget();
        Component replacement = getReplacement();
        TextReplacementConfig.Builder builder = TextReplacementConfig.builder();
        return builder.matchLiteral(target).replacement(replacement).build();
    }
}
