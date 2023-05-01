package com.github.sirblobman.api.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.menu.task.CloseMenuTask;

public final class ExitButton extends QuickButton {
    private final IMenu menu;

    public ExitButton(@NotNull IMenu menu) {
        this.menu = menu;
    }

    private @NotNull IMenu getMenu() {
        return this.menu;
    }

    private @NotNull Plugin getPlugin() {
        IMenu menu = getMenu();
        return menu.getPlugin();
    }

    private @NotNull TaskScheduler getTaskScheduler() {
        IMenu menu = getMenu();
        return menu.getTaskScheduler();
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        Plugin plugin = getPlugin();
        CloseMenuTask task = new CloseMenuTask(plugin, player);
        TaskScheduler scheduler = getTaskScheduler();
        scheduler.scheduleEntityTask(task);
    }
}
