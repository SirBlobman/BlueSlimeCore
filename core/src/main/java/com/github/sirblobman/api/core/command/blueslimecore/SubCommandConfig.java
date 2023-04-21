package com.github.sirblobman.api.core.command.blueslimecore;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.core.menu.ConfigurationSelectorMenu;

public final class SubCommandConfig extends PlayerCommand {
    private final CorePlugin plugin;

    public SubCommandConfig(@NotNull CorePlugin plugin) {
        super(plugin, "config");
        setPermissionName("blue.slime.core.command.blueslimecore.config");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull Player player, String @NotNull [] args) {
        CorePlugin plugin = getCorePlugin();
        new ConfigurationSelectorMenu(plugin, player).open();
        return true;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }
}
