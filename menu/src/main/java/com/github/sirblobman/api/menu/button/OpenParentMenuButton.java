package com.github.sirblobman.api.menu.button;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.menu.task.MenuOpenTask;

public final class OpenParentMenuButton<P extends Plugin> extends QuickButton {
    private final IMenu menu;

    public OpenParentMenuButton(@NotNull IMenu menu) {
        this.menu = menu;
    }

    private @NotNull IMenu getMenu() {
        return this.menu;
    }

    private @NotNull Optional<IMenu> getParentMenu() {
        IMenu menu = getMenu();
        return menu.getParentMenu();
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        Optional<IMenu> optionalParentMenu = getParentMenu();
        if (!optionalParentMenu.isPresent()) {
            return;
        }

        IMenu parentMenu = optionalParentMenu.get();
        Plugin plugin = parentMenu.getPlugin();

        MenuOpenTask task = new MenuOpenTask(plugin, player, parentMenu);
        TaskScheduler scheduler = parentMenu.getTaskScheduler();
        scheduler.scheduleEntityTask(task);
    }
}
