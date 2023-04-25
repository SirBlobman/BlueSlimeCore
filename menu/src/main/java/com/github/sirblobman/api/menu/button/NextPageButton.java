package com.github.sirblobman.api.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.menu.AbstractPagedMenu;

public final class NextPageButton extends QuickButton {
    private final AbstractPagedMenu<?> pagedMenu;

    public NextPageButton(@NotNull AbstractPagedMenu<?> pagedMenu) {
        this.pagedMenu = pagedMenu;
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        AbstractPagedMenu<?> pagedMenu = getPagedMenu();
        pagedMenu.openNextPage();
    }

    private @NotNull AbstractPagedMenu<?> getPagedMenu() {
        return this.pagedMenu;
    }
}
