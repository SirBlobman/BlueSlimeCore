package com.github.sirblobman.api.language.replacer;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.ComponentLike;
import com.github.sirblobman.api.shaded.adventure.text.serializer.plain.PlainTextComponentSerializer;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class TranslatableReplacer extends Replacer {
    private final String translationKey;
    private final ComponentLike[] args;

    public TranslatableReplacer(String target, String translationKey, ComponentLike... args) {
        super(target);
        this.translationKey = Validate.notEmpty(translationKey, "translation key must not be empty!");
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
