package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.menu.AdvancedAbstractMenu;

public final class AdvancedMenuInternalOpenTask<P extends Plugin> extends EntityTaskDetails<P, Player> {
    private final AdvancedAbstractMenu<P> menu;

    public AdvancedMenuInternalOpenTask(@NotNull P plugin, @NotNull Player entity,
                                        @NotNull AdvancedAbstractMenu<P> menu) {
        super(plugin, entity);
        this.menu = menu;
    }

    private @NotNull AdvancedAbstractMenu<P> getMenu() {
        return this.menu;
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        P plugin = getPlugin();
        AdvancedAbstractMenu<P> menu = getMenu();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(menu, plugin);

        Inventory inventory = menu.getInventory();
        player.openInventory(inventory);
    }
}
