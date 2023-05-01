package com.github.sirblobman.api.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.menu.IMenu;

public final class OpenMenuButton extends QuickButton {
    private final IMenu menu;

    public OpenMenuButton(@NotNull IMenu menu) {
        this.menu = menu;
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        IMenu menu = getMenu();
        menu.open();
    }

    private @NotNull IMenu getMenu() {
        return this.menu;
    }
}
