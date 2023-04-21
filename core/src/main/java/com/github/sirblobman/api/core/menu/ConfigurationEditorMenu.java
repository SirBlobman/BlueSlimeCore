package com.github.sirblobman.api.core.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.menu.AbstractPagedMenu;
import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.menu.button.AbstractButton;
import com.github.sirblobman.api.shaded.adventure.text.Component;

// TODO
public final class ConfigurationEditorMenu extends AbstractPagedMenu {
    private final YamlConfiguration configuration;

    public ConfigurationEditorMenu(@NotNull IMenu parent, @NotNull Plugin plugin, @NotNull Player player,
                                   @NotNull YamlConfiguration configuration) {
        super(parent, plugin, player);
        this.configuration = configuration;
    }

    public @NotNull YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return null;
    }

    @Override
    public @Nullable AbstractButton getButton(int slot) {
        return null;
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return false;
    }

    @Override
    public int getMaxPages() {
        return 0;
    }

    @Override
    public @Nullable Component getTitleFormat() {
        return null;
    }
}
