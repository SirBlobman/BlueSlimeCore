package com.github.sirblobman.api.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ConsoleCommand extends Command {
    public ConsoleCommand(JavaPlugin plugin, String commandName) {
        super(plugin, commandName);
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            return onTabComplete(console, args);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            return execute(console, args);
        }

        sendMessageOrDefault(sender, "error.console-only",
                "That command can only be executed in the server console.", null, true);
        return true;
    }

    /**
     * @param console The console that is tab-completing this command.
     * @param args An array of command arguments.
     * @return The list of tab completions for this combination of sender and command arguments.
     */
    protected abstract List<String> onTabComplete(ConsoleCommandSender console, String[] args);

    /**
     * @param console The console that is executing this command.
     * @param args An array of command arguments.
     * @return {@code true} if the command was executed correctly,
     * {@code false} if the sender needs to see the command usage.
     */
    protected abstract boolean execute(ConsoleCommandSender console, String[] args);
}
