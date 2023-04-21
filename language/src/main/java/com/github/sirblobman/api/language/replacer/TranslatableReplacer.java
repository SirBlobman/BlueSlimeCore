package com.github.sirblobman.api.language.replacer;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.ComponentLike;
import com.github.sirblobman.api.shaded.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class TranslatableReplacer extends Replacer {
    private final String translationKey;
    private final ComponentLike[] args;

    public TranslatableReplacer(@NotNull String target, @NotNull String translationKey,
                                ComponentLike @NotNull ... args) {
        super(target);
        this.translationKey = translationKey;
        this.args = args;
    }

    @Override
    public @NotNull Component getReplacement() {
        return Component.translatable(this.translationKey, this.args);
    }

    @Override
    public @NotNull String getReplacementString() {
        Component replacement = getReplacement();
        PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
        return serializer.serialize(replacement);
    }
}
