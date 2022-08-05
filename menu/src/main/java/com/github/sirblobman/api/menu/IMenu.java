package com.github.sirblobman.api.menu;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public interface IMenu extends InventoryHolder {
    /**
     * Use this method to open the menu.
     */
    void open();

    /**
     * @return The plugin that owns this menu.
     */
    JavaPlugin getPlugin();

    /**
     * Override this method to use a custom close action.
     *
     * @param e the InventoryCloseEvent of the action.
     */
    void onCustomClose(InventoryCloseEvent e);
}
