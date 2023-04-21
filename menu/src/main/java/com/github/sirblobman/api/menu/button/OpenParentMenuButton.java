package com.github.sirblobman.api.menu.button;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.menu.IMenu;

public final class OpenParentMenuButton extends QuickButton {
    private final IMenu currentMenu;

    public OpenParentMenuButton(@NotNull IMenu currentMenu) {
        this.currentMenu = currentMenu;
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        Optional<IMenu> previousMenu = getPreviousMenu();
        if (!previousMenu.isPresent()) {
            return;
        }

        Runnable task = () -> {
            IMenu parentMenu = previousMenu.get();
            parentMenu.open();
        };

        Plugin plugin = currentMenu.getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, task, 2L);
    }

    private @NotNull IMenu getCurrentMenu() {
        return this.currentMenu;
    }

    private @NotNull Optional<IMenu> getPreviousMenu() {
        IMenu currentMenu = getCurrentMenu();
        return currentMenu.getParentMenu();
    }
}
