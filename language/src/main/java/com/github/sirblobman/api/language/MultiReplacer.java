package com.github.sirblobman.api.language;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A simple {@link Replacer} that replaces all strings that match the target with the replacement.
 *
 * @see String#replace(CharSequence, CharSequence)
 */
public final class MultiReplacer implements Replacer {
    private final Map<String, String> replacementMap;

    public MultiReplacer(String firstTarget, String firstReplacement) {
        this.replacementMap = new LinkedHashMap<>();
        this.replacementMap.put(firstTarget, firstReplacement);
    }

    public Map<String, String> getReplacementMap() {
        return Collections.unmodifiableMap(this.replacementMap);
    }

    public MultiReplacer addReplacement(String target, String replacement) {
        if(this.replacementMap.containsKey(target)) {
            String mapReplacement = this.replacementMap.get(target);
            throw new IllegalArgumentException("'" + target + "' is already being replaced with '"
                    + mapReplacement + "'.");
        }

        this.replacementMap.put(target, replacement);
        return this;
    }

    @Override
    public String replace(String string) {
        Map<String, String> replacementMap = getReplacementMap();
        Set<Entry<String, String>> replacementMapEntrySet = replacementMap.entrySet();
        for (Entry<String, String> entry : replacementMapEntrySet) {
            String target = entry.getKey();
            String replacement = entry.getValue();
            string = string.replace(target, replacement);
        }

        return string;
    }
}
