package com.github.sirblobman.api.language.custom;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class PlayerListInfo {
    private Component header;
    private Component footer;

    public PlayerListInfo() {
        this.header = Component.empty();
        this.footer = Component.empty();
    }

    public @NotNull Component getHeader() {
        return this.header;
    }

    public void setHeader(@NotNull Component header) {
        this.header = header;
    }

    public @NotNull Component getFooter() {
        return this.footer;
    }

    public void setFooter(@NotNull Component footer) {
        this.footer = footer;
    }
}
