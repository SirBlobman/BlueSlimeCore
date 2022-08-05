package com.github.sirblobman.api.menu.button;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.utility.Validate;

public final class OpenMenuButton extends QuickButton {
    private final IMenu menu;

    public OpenMenuButton(IMenu menu) {
        this.menu = Validate.notNull(menu, "menu must not be null!");
    }

    @Override
    public void onLeftClick(Player player, boolean shift) {
        IMenu menu = getMenu();
        menu.open();
    }

    private IMenu getMenu() {
        return this.menu;
    }
}
