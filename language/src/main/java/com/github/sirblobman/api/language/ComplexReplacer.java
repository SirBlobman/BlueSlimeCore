package com.github.sirblobman.api.language;

import com.github.sirblobman.api.utility.Validate;

public final class ComplexReplacer implements Replacer {
    private final Replacer[] replacerArray;

    public ComplexReplacer(Replacer... replacerArray) {
        this.replacerArray = Validate.notNull(replacerArray, "replacerArray must not be null!");
    }

    public Replacer[] getReplacerArray() {
        return this.replacerArray;
    }

    @Override
    public String replace(String string) {
        Replacer[] replacerArray = getReplacerArray();
        for (Replacer replacer : replacerArray) {
            if (replacer == null) {
                continue;
            }

            string = replacer.replace(string);
        }

        return string;
    }
}
