package com.github.sirblobman.api.language;

import com.github.sirblobman.api.utility.Validate;

/**
 * A simple {@link Replacer} that replaces all strings that match the target with the replacement.
 *
 * @see String#replace(CharSequence, CharSequence)
 */
public final class SimpleReplacer implements Replacer {
    private final String target;
    private final String replacement;

    public SimpleReplacer(String target, String replacement) {
        this.target = Validate.notEmpty(target, "target must not be empty!");
        this.replacement = Validate.notNull(replacement, "replacement must not be null!");
    }

    public String getTargetString() {
        return this.target;
    }

    public String getReplacementString() {
        return this.replacement;
    }

    @Override
    public String replace(String string) {
        String target = getTargetString();
        String replacement = getReplacementString();
        return string.replace(target, replacement);
    }
}
