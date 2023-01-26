package com.github.sirblobman.api.core.command.blueslimecore;

import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.core.menu.ConfigurationSelectorMenu;

public final class SubCommandConfig extends PlayerCommand {
    private final CorePlugin plugin;

    public SubCommandConfig(CorePlugin plugin) {
        super(plugin, "config");
        setPermissionName("blue.slime.core.command.blueslimecore.config");
        this.plugin = plugin;
    }

    @Override
    protected List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        CorePlugin plugin = getCorePlugin();
        new ConfigurationSelectorMenu(plugin, player).open();
        return true;
    }

    private CorePlugin getCorePlugin() {
        return this.plugin;
    }
}
