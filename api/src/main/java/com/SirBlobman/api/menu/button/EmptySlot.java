package com.SirBlobman.api.menu.button;

import org.bukkit.event.inventory.InventoryClickEvent;

public class EmptySlot extends MenuButton {
    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(false);
    }
}