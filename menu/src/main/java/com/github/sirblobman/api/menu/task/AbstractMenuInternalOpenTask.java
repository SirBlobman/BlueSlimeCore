package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.menu.AbstractMenu;

public final class AbstractMenuInternalOpenTask extends EntityTaskDetails<Player> {
    private final AbstractMenu<?> menu;

    public AbstractMenuInternalOpenTask(@NotNull Plugin plugin, @NotNull Player player,
                                        @NotNull AbstractMenu<?> menu) {
        super(plugin, player);
        this.menu = menu;
    }

    private @NotNull AbstractMenu<?> getMenu() {
        return this.menu;
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            return;
        }

        AbstractMenu<?> menu = getMenu();
        menu.resetButtons();

        Plugin plugin = getPlugin();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(menu, plugin);

        Inventory inventory = menu.getInventory();
        player.openInventory(inventory);
    }
}
