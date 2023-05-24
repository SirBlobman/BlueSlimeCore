package com.github.sirblobman.api.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ConsoleCommand extends Command {
    public ConsoleCommand(@NotNull JavaPlugin plugin, @NotNull String commandName) {
        super(plugin, commandName);
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            return onTabComplete(console, args);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            return execute(console, args);
        }

        sendMessage(sender, "error.console-only");
        return true;
    }

    /**
     * @param console The console that is tab-completing this command.
     * @param args    An array of command arguments.
     * @return The list of tab completions for this combination of sender and command arguments.
     */
    protected abstract @NotNull List<String> onTabComplete(@NotNull ConsoleCommandSender console,
                                                           String @NotNull [] args);

    /**
     * @param console The console that is executing this command.
     * @param args    An array of command arguments.
     * @return {@code true} if the command was executed correctly, {@code false} if the sender needs to see the command
     * usage.
     */
    protected abstract boolean execute(@NotNull ConsoleCommandSender console, String @NotNull [] args);
}
