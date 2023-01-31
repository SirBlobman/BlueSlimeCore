package com.github.sirblobman.api.language.custom;

import com.github.sirblobman.api.adventure.adventure.text.Component;

import org.jetbrains.annotations.NotNull;

public final class ModifiableMessage {
    private Component message;
    private ModifiableMessageType type;

    public ModifiableMessage() {
        this.message = Component.empty();
        this.type = ModifiableMessageType.CHAT;
    }

    @NotNull
    public Component getMessage() {
        return this.message;
    }

    public void setMessage(@NotNull Component message) {
        this.message = message;
    }

    @NotNull
    public ModifiableMessageType getType() {
        return this.type;
    }

    public void setType(@NotNull ModifiableMessageType type) {
        this.type = type;
    }
}
