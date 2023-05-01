package com.github.sirblobman.api.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface IButton {
    void onClick(@NotNull InventoryClickEvent e);
}
