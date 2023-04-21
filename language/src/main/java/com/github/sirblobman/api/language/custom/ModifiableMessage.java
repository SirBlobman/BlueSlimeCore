package com.github.sirblobman.api.language.custom;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class ModifiableMessage {
    private Component message;
    private ModifiableMessageType type;

    public ModifiableMessage() {
        this.message = Component.empty();
        this.type = ModifiableMessageType.CHAT;
    }

    public @NotNull Component getMessage() {
        return this.message;
    }

    public void setMessage(@NotNull Component message) {
        this.message = message;
    }

    public @NotNull ModifiableMessageType getType() {
        return this.type;
    }

    public void setType(@NotNull ModifiableMessageType type) {
        this.type = type;
    }
}
