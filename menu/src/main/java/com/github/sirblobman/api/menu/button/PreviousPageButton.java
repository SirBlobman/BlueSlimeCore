package com.github.sirblobman.api.menu.button;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.menu.AbstractPagedMenu;
import com.github.sirblobman.api.utility.Validate;

public final class PreviousPageButton extends QuickButton {
    private final AbstractPagedMenu pagedMenu;
    
    public PreviousPageButton(AbstractPagedMenu pagedMenu) {
        this.pagedMenu = Validate.notNull(pagedMenu, "pagedMenu must not be null!");
    }
    
    @Override
    public void onLeftClick(Player player, boolean shift) {
        this.pagedMenu.openPreviousPage();
    }
}
