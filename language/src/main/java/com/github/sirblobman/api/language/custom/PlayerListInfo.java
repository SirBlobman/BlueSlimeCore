package com.github.sirblobman.api.language.custom;

import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class PlayerListInfo {
    private Component header;
    private Component footer;

    public PlayerListInfo() {
        this.header = Component.empty();
        this.footer = Component.empty();
    }

    public Component getHeader() {
        return this.header;
    }

    public void setHeader(Component header) {
        this.header = header;
    }

    public Component getFooter() {
        return this.footer;
    }

    public void setFooter(Component footer) {
        this.footer = footer;
    }
}
