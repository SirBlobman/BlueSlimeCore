package com.github.sirblobman.api.core.command.sirblobmancore;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;

public final class CommandSirBlobmanCore extends Command {
    public CommandSirBlobmanCore(CorePlugin plugin) {
        super(plugin, "sirblobmancore");
        addSubCommand(new CommandSirBlobmanCoreHelp(plugin));
        addSubCommand(new CommandSirBlobmanCoreReload(plugin));
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
