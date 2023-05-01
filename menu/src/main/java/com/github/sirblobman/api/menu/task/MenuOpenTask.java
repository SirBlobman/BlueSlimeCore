package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.menu.IMenu;

public final class MenuOpenTask extends EntityTaskDetails<Player> {
    private final IMenu menu;

    public MenuOpenTask(@NotNull Plugin plugin, @NotNull Player entity, @NotNull IMenu menu) {
        super(plugin, entity);
        this.menu = menu;
    }

    private @NotNull IMenu getMenu() {
        return this.menu;
    }

    @Override
    public void run() {
        Player entity = getEntity();
        if (entity == null) {
            return;
        }

        IMenu menu = getMenu();
        menu.open();
    }
}
