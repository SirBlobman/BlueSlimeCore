package com.github.sirblobman.api.menu;

import java.util.Optional;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.plugin.IMultiVersionPlugin;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public interface IMenu extends InventoryHolder, Listener {
    /**
     * @return The plugin that owns this menu.
     */
    @NotNull Plugin getPlugin();

    default @NotNull Logger getLogger() {
        Plugin plugin = getPlugin();
        return plugin.getLogger();
    }

    /**
     * Use this method to open the menu.
     */
    void open();

    /**
     * @return The title component for the menu.
     */
    default @Nullable Component getTitle() {
        return null;
    }

    /**
     * Override this method to use a custom close action.
     *
     * @param e the InventoryCloseEvent of the action.
     */
    void onCustomClose(@NotNull InventoryCloseEvent e);

    /**
     * Used when there is a button to open the previous menu,
     * or if closing the menu causes the previous one to open.
     *
     * @return An optional menu that caused this menu to open.
     */
    @NotNull Optional<IMenu> getParentMenu();

    /**
     * Change the parent menu for this menu.
     *
     * @param parentMenu The new parent menu. Can be null.
     * @see #getParentMenu()
     */
    void setParentMenu(@NotNull IMenu parentMenu);

    default @Nullable MultiVersionHandler getMultiVersionHandler() {
        Plugin plugin = getPlugin();
        if (plugin instanceof IMultiVersionPlugin) {
            IMultiVersionPlugin multiVersionPlugin = (IMultiVersionPlugin) plugin;
            return multiVersionPlugin.getMultiVersionHandler();
        }

        return null;
    }

    /**
     * @return The language manager if your plugin has one.
     */
    default @Nullable LanguageManager getLanguageManager() {
        return null;
    }
}
