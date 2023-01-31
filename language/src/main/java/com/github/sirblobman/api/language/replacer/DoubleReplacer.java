package com.github.sirblobman.api.language.replacer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.github.sirblobman.api.adventure.adventure.text.Component;

import org.jetbrains.annotations.NotNull;

public final class DoubleReplacer extends Replacer {
    private final double replacement;
    private final DecimalFormat format;
    private final DecimalFormatSymbols symbols;

    public DoubleReplacer(String target, double replacement) {
        this(target, replacement, null);
    }

    public DoubleReplacer(String target, double replacement, DecimalFormat format) {
        this(target, replacement, format, null);
    }

    public DoubleReplacer(String target, double replacement, DecimalFormat format, DecimalFormatSymbols symbols) {
        super(target);
        this.replacement = replacement;
        this.format = format;
        this.symbols = symbols;
    }

    @Override
    public @NotNull Component getReplacement() {
        if (this.format == null) {
            return Component.text(this.replacement);
        }

        DecimalFormat decimalFormat = this.format;
        if (this.symbols != null) {
            decimalFormat = (DecimalFormat) decimalFormat.clone();
            decimalFormat.setDecimalFormatSymbols(this.symbols);
        }

        String formatted = decimalFormat.format(this.replacement);
        return Component.text(formatted);
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
