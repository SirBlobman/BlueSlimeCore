package com.github.sirblobman.api.menu.button;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.IFoliaPlugin;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.menu.task.CloseInventoryTask;

public final class ExitButton<P extends Plugin> extends QuickButton {
    private final IFoliaPlugin<P> plugin;

    public ExitButton(@NotNull IMenu<P> menu) {
        this.plugin = menu.getFoliaPlugin();
    }

    @Override
    public void onLeftClick(@NotNull Player player, boolean shift) {
        IFoliaPlugin<P> plugin = getPlugin();
        FoliaHelper<P> foliaHelper = plugin.getFoliaHelper();
        TaskScheduler<P> scheduler = foliaHelper.getScheduler();

        P bukkitPlugin = plugin.getPlugin();
        CloseInventoryTask<P> task = new CloseInventoryTask<>(bukkitPlugin, player);
        scheduler.scheduleEntityTask(task);
    }

    private @NotNull IFoliaPlugin<P> getPlugin() {
        return this.plugin;
    }
}
