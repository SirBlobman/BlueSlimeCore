package com.github.sirblobman.api.core.command.blueslimecore;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;

public final class SubCommandHelp extends Command {
    public SubCommandHelp(CorePlugin plugin) {
        super(plugin, "help");
        setPermissionName("blue.slime.core.command.blueslimecore.help");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        sendMessage(sender, "command.blueslimecore.help.title", null);
        sendMessage(sender, "command.blueslimecore.help.command-list", null);
        return true;
    }
}
