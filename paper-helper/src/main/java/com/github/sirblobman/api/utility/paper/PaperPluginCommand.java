package com.github.sirblobman.api.utility.paper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import org.apache.commons.lang.Validate;

public class PaperPluginCommand extends Command implements PluginIdentifiableCommand {
    private final Plugin plugin;
    private final CommandData commandData;

    protected PaperPluginCommand(@NotNull Plugin plugin, @NotNull String name, @NotNull CommandData commandData) {
        super(name);
        this.plugin = plugin;
        this.commandData = commandData;

        String usage = commandData.getUsage();
        if (usage != null) {
            setUsage(usage);
        }

        String description = commandData.getDescription();
        if (description != null) {
            setDescription(description);
        }

        String permission = commandData.getPermission();
        if (permission != null) {
            setPermission(permission);
        }

        String permissionMessage = commandData.getPermissionMessage();
        if (permissionMessage != null) {
            setPermissionMessage(permissionMessage);
        }

        Set<String> aliases = commandData.getAliases();
        setAliases(new ArrayList<>(aliases));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        boolean success;
        Plugin plugin = getPlugin();
        CommandData commandData = getCommandData();
        CommandExecutor executor = commandData.getCommandExecutor();

        if (!plugin.isEnabled()) {
            throw new CommandException("Cannot execute command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName() + " - plugin is disabled.");
        }

        if (!testPermission(sender)) {
            return true;
        }

        try {
            success = executor.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }

        String usageMessage = commandData.getUsage();
        if(usageMessage == null) {
            usageMessage = "";
        }

        if (!success && !usageMessage.isEmpty()) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return success;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        CommandData commandData = getCommandData();
        TabCompleter completer = commandData.getTabExecutor();
        CommandExecutor executor = commandData.getCommandExecutor();

        List<String> completions = null;
        try {
            if (completer != null) {
                completions = completer.onTabComplete(sender, this, alias, args);
            }
            if (completions == null && executor instanceof TabCompleter) {
                completions = ((TabCompleter) executor).onTabComplete(sender, this, alias, args);
            }
        } catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
            for (String arg : args) {
                message.append(arg).append(' ');
            }
            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(plugin.getDescription().getFullName());
            throw new CommandException(message.toString(), ex);
        }

        if (completions == null) {
            return super.tabComplete(sender, alias, args);
        }
        return completions;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    public @NotNull CommandData getCommandData() {
        return commandData;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(", ").append(plugin.getDescription().getFullName()).append(')');
        return stringBuilder.toString();
    }
}
