package com.github.sirblobman.api.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class QuickButton extends AbstractButton {
    @Override
    public void onClick(@NotNull InventoryClickEvent e) {
        HumanEntity human = e.getWhoClicked();
        if (!(human instanceof Player)) {
            return;
        }

        Player player = (Player) human;
        ClickType clickType = e.getClick();
        boolean shift = clickType.isShiftClick();

        if (clickType.isLeftClick()) {
            onLeftClick(player, shift);
            return;
        }

        if (clickType.isRightClick()) {
            onRightClick(player, shift);
            return;
        }

        if (clickType == ClickType.MIDDLE) {
            onMiddleClick(player, shift);
        }
    }

    public void onLeftClick(@NotNull Player player, boolean shift) {
        // Do Nothing
    }

    public void onRightClick(@NotNull Player player, boolean shift) {
        // Do Nothing
    }

    public void onMiddleClick(@NotNull Player player, boolean shift) {
        // Do Nothing
    }
}
