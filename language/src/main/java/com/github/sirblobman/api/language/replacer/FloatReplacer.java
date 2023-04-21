package com.github.sirblobman.api.language.replacer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class FloatReplacer extends Replacer {
    private final float replacement;
    private final DecimalFormat format;
    private final DecimalFormatSymbols symbols;

    public FloatReplacer(@NotNull String target, float replacement) {
        this(target, replacement, null);
    }

    public FloatReplacer(@NotNull String target, float replacement, @Nullable DecimalFormat format) {
        this(target, replacement, format, null);
    }

    public FloatReplacer(@NotNull String target, float replacement,
                         @Nullable DecimalFormat format, @Nullable DecimalFormatSymbols symbols) {
        super(target);
        this.replacement = replacement;
        this.format = format;
        this.symbols = symbols;
    }

    @Override
    public @NotNull Component getReplacement() {
        String replacement = getReplacementString();
        return Component.text(replacement);
    }

    @Override
    public @NotNull String getReplacementString() {
        if (this.format == null) {
            return Double.toString(this.replacement);
        }

        DecimalFormat decimalFormat = this.format;
        if (this.symbols != null) {
            decimalFormat = (DecimalFormat) decimalFormat.clone();
            decimalFormat.setDecimalFormatSymbols(this.symbols);
        }

        return decimalFormat.format(this.replacement);
    }
}
