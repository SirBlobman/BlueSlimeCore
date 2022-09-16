package com.github.sirblobman.api.core.command.blueslimecore;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;

public final class CommandBlueSlimeCore extends Command {
    public CommandBlueSlimeCore(CorePlugin plugin) {
        super(plugin, "blueslimecore");
        setPermissionName("blue.slime.core.command.blueslimecore");
        addSubCommand(new SubCommandHelp(plugin));
        addSubCommand(new SubCommandLanguageTest(plugin));
        addSubCommand(new SubCommandReload(plugin));
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        return false;
    }
}
