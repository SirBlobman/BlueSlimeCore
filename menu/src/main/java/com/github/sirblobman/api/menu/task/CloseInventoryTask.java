package com.github.sirblobman.api.menu.task;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.details.EntityTaskDetails;

public final class CloseInventoryTask<P extends Plugin> extends EntityTaskDetails<P, HumanEntity> {
    public CloseInventoryTask(@NotNull P plugin, @NotNull HumanEntity entity) {
        super(plugin, entity);
        setDelay(2L);
    }

    @Override
    public void run() {
        HumanEntity entity = getEntity();
        if (entity != null) {
            entity.closeInventory();
        }
    }
}
