package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.menu.AdvancedAbstractMenu;

public final class AdvancedMenuInternalOpenTask extends EntityTaskDetails<Player> {
    private final AdvancedAbstractMenu<?> menu;

    public AdvancedMenuInternalOpenTask(@NotNull Plugin plugin, @NotNull Player player,
                                        @NotNull AdvancedAbstractMenu<?> menu) {
        super(plugin, player);
        this.menu = menu;
    }

    private @NotNull AdvancedAbstractMenu<?> getMenu() {
        return this.menu;
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        Plugin plugin = getPlugin();
        AdvancedAbstractMenu<?> menu = getMenu();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(menu, plugin);

        Inventory inventory = menu.getInventory();
        player.openInventory(inventory);
    }
}
