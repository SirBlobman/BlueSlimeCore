package com.github.sirblobman.api.menu.button;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class OpenParentMenuButton extends QuickButton {
    private final IMenu currentMenu;

    public OpenParentMenuButton(IMenu currentMenu) {
        this.currentMenu = Validate.notNull(currentMenu, "currentMenu must not be null!");
    }

    @Override
    public void onLeftClick(Player player, boolean shift) {
        Optional<IMenu> previousMenu = getPreviousMenu();
        if (!previousMenu.isPresent()) {
            return;
        }

        Runnable task = () -> {
            IMenu parentMenu = previousMenu.get();
            parentMenu.open();
        };

        JavaPlugin plugin = currentMenu.getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, task, 2L);
    }

    @NotNull
    private IMenu getCurrentMenu() {
        return this.currentMenu;
    }

    private Optional<IMenu> getPreviousMenu() {
        IMenu currentMenu = getCurrentMenu();
        return currentMenu.getParentMenu();
    }
}
