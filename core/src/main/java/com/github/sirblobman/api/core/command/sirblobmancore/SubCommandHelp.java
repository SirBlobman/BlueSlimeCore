package com.github.sirblobman.api.core.command.sirblobmancore;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;

public class SubCommandHelp extends Command {
    public SubCommandHelp(CorePlugin plugin) {
        super(plugin, "help");
        setPermissionName("sirblobman.core.command.sirblobmancore.help");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        sendMessage(sender, "command.sirblobmancore.help.title", null, true);
        sendMessage(sender, "command.sirblobmancore.help.command-list", null, true);
        return true;
    }
}
