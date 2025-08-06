package com.github.sirblobman.api.menu.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import com.github.sirblobman.api.menu.AdvancedAbstractMenu;

public final class NewInventoryListener implements Listener {
    private final AdvancedAbstractMenu<?> menu;

    public NewInventoryListener(@NotNull AdvancedAbstractMenu<?> menu) {
        this.menu = menu;
    }

    private @NotNull AdvancedAbstractMenu<?> getMenu() {
        return this.menu;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onClose(InventoryCloseEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        InventoryHolder inventoryHolder = topInventory.getHolder();

        AdvancedAbstractMenu<?> menu = getMenu();
        if (!menu.equals(inventoryHolder)) {
            return;
        }

        menu.internalClose();
        menu.onCustomClose(e);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        InventoryHolder inventoryHolder = topInventory.getHolder();

        AdvancedAbstractMenu<?> menu = getMenu();
        if (!menu.equals(inventoryHolder)) {
            return;
        }

        menu.onValidClick(e);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDrag(InventoryDragEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        InventoryHolder inventoryHolder = topInventory.getHolder();

        AdvancedAbstractMenu<?> menu = getMenu();
        if (!menu.equals(inventoryHolder)) {
            return;
        }

        menu.onValidDrag(e);
    }
}
