package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.menu.AdvancedAbstractMenu;

public final class AdvancedMenuRefreshLoopTask<P extends Plugin> extends EntityTaskDetails<P, Player> {
    private final AdvancedAbstractMenu<P> menu;

    public AdvancedMenuRefreshLoopTask(@NotNull P plugin, @NotNull Player entity,
                                       @NotNull AdvancedAbstractMenu<P> menu) {
        super(plugin, entity);
        setDelay(20L);
        setPeriod(20L);
        this.menu = menu;
    }

    private @NotNull AdvancedAbstractMenu<P> getMenu() {
        return this.menu;
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            cancel();
            return;
        }

        AdvancedAbstractMenu<P> menu = getMenu();
        menu.run();
    }
}
