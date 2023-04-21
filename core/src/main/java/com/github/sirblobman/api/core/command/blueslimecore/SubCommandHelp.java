package com.github.sirblobman.api.core.command.blueslimecore;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;

public final class SubCommandHelp extends Command {
    public SubCommandHelp(@NotNull CorePlugin plugin) {
        super(plugin, "help");
        setPermissionName("blue.slime.core.command.blueslimecore.help");
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        sendMessage(sender, "command.blueslimecore.help.title");
        sendMessage(sender, "command.blueslimecore.help.command-list");
        return true;
    }
}
