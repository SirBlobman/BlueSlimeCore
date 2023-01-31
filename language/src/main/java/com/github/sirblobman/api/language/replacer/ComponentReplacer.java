package com.github.sirblobman.api.language.replacer;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.adventure.adventure.text.serializer.plain.PlainTextComponentSerializer;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class ComponentReplacer extends Replacer {
    private final Component replacement;

    public ComponentReplacer(String target, Component replacement) {
        super(target);
        this.replacement = Validate.notNull(replacement, "replacement must not be null!");
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
