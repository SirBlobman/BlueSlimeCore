package com.github.sirblobman.api.language.replacer;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.TextReplacementConfig;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public abstract class Replacer {
    private final String target;

    public Replacer(String target) {
        this.target = Validate.notEmpty(target, "target must not be empty!");
    }

    public String getTarget() {
        return this.target;
    }

    @NotNull
    public abstract Component getReplacement();

    @NotNull
    public abstract String getReplacementString();

    @NotNull
    public final TextReplacementConfig asReplacementConfig() {
        String target = getTarget();
        Component replacement = getReplacement();
        TextReplacementConfig.Builder builder = TextReplacementConfig.builder();
        return builder.matchLiteral(target).replacement(replacement).build();
    }
}
