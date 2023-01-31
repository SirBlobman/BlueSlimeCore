package com.github.sirblobman.api.language.custom;

import java.util.Locale;

public enum ModifiableMessageType {
    /**
     * A message sent to the chat box.
     */
    CHAT,

    /**
     * A message shown in the action bar above the inventory hotbar.
     */
    ACTION_BAR;

    public static ModifiableMessageType parse(String value) {
        try {
            String uppercase = value.toUpperCase(Locale.US);
            String underscore = uppercase.replace(' ', '_').replace('-', '_');
            return valueOf(underscore);
        } catch(IllegalArgumentException ex) {
            return CHAT;
        }
    }
}
