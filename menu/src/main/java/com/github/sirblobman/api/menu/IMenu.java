package com.github.sirblobman.api.menu;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public interface IMenu extends InventoryHolder, Listener {
    /**
     * @return The plugin that owns this menu.
     */
    JavaPlugin getPlugin();

    /**
     * Use this method to open the menu.
     */
    void open();

    /**
     * Override this method to use a custom close action.
     *
     * @param e the InventoryCloseEvent of the action.
     */
    void onCustomClose(InventoryCloseEvent e);
}
