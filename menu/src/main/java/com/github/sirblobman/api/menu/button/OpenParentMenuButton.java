package com.github.sirblobman.api.menu.button;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.menu.IMenu;

public final class OpenParentMenuButton<P extends Plugin> extends QuickButton {
    private final IMenu<P> currentMenu;

    public OpenParentMenuButton(@NotNull IMenu<P> currentMenu) {
        this.currentMenu = currentMenu;
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        Optional<IMenu<P>> previousMenu = getPreviousMenu();
        if (!previousMenu.isPresent()) {
            return;
        }

        IMenu<P> currentMenu = getCurrentMenu();
        P plugin = currentMenu.getPlugin();
        EntityTaskDetails<P, Player> task = new EntityTaskDetails<P, Player>(plugin, player) {
            @Override
            public void run() {
                IMenu<P> parentMenu = previousMenu.get();
                parentMenu.open();
            }
        };

        task.setDelay(2L);
        TaskScheduler<P> scheduler = currentMenu.getFoliaPlugin().getFoliaHelper().getScheduler();
        scheduler.scheduleEntityTask(task);
    }

    private @NotNull IMenu<P> getCurrentMenu() {
        return this.currentMenu;
    }

    private @NotNull Optional<IMenu<P>> getPreviousMenu() {
        IMenu<P> currentMenu = getCurrentMenu();
        return currentMenu.getParentMenu();
    }
}
