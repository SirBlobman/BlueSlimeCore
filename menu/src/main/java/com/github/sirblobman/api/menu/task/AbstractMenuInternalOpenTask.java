package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.menu.AbstractMenu;

public final class AbstractMenuInternalOpenTask<P extends Plugin> extends EntityTaskDetails<P, Player> {
    private final AbstractMenu<P> menu;

    public AbstractMenuInternalOpenTask(@NotNull P plugin, @NotNull Player entity, @NotNull AbstractMenu<P> menu) {
        super(plugin, entity);
        this.menu = menu;
    }

    private @NotNull AbstractMenu<P> getMenu() {
        return this.menu;
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        AbstractMenu<P> menu = getMenu();
        menu.resetButtons();

        P plugin = getPlugin();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(menu, plugin);

        Inventory inventory = menu.getInventory();
        player.openInventory(inventory);
    }
}
