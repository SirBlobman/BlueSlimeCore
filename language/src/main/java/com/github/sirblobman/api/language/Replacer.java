package com.github.sirblobman.api.language;

@FunctionalInterface
public interface Replacer {
    /**
     * Replaces values in a string
     *
     * @param string The string with values to replace
     * @return A new string with the values replaced
     */
    String replace(String string);
}
