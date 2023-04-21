package com.github.sirblobman.api.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.utility.Validate;

public final class ExitButton extends QuickButton {
    private final Plugin plugin;

    public ExitButton(@NotNull IMenu menu) {
        Validate.notNull(menu, "menu must not be null!");
        this.plugin = menu.getPlugin();
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        Plugin plugin = getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, player::closeInventory, 2L);
    }

    private @NotNull Plugin getPlugin() {
        return this.plugin;
    }
}
