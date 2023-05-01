package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.menu.AdvancedAbstractMenu;

public final class AdvancedMenuRefreshLoopTask extends EntityTaskDetails<Player> {
    private final AdvancedAbstractMenu<?> menu;

    public AdvancedMenuRefreshLoopTask(@NotNull Plugin plugin, @NotNull Player player,
                                       @NotNull AdvancedAbstractMenu<?> menu) {
        super(plugin, player);
        setDelay(20L);
        setPeriod(20L);
        this.menu = menu;
    }

    private @NotNull AdvancedAbstractMenu<?> getMenu() {
        return this.menu;
    }

    @Override
    public void run() {
        Player player = getEntity();
        if (player == null) {
            cancel();
            return;
        }

        AdvancedAbstractMenu<?> menu = getMenu();
        menu.run();
    }
}
