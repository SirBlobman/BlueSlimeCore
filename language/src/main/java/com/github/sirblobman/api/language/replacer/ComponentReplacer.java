package com.github.sirblobman.api.language.replacer;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class ComponentReplacer extends Replacer {
    private final Component replacement;

    public ComponentReplacer(@NotNull String target, @NotNull Component replacement) {
        super(target);
        this.replacement = replacement;
    }

    @Override
    public @NotNull Component getReplacement() {
        return this.replacement;
    }

    @Override
    public @NotNull String getReplacementString() {
        Component replacement = getReplacement();
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        return serializer.serialize(replacement);
    }
}
