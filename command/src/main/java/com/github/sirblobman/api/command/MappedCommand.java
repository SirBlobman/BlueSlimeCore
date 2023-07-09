package com.github.sirblobman.api.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.Validate;

class MappedCommand extends Command {
    private final JavaPlugin plugin;
    private final Permission permission;
    private final com.github.sirblobman.api.command.Command originalCommand;

    protected MappedCommand(@NotNull com.github.sirblobman.api.command.Command command) {
        super(command.getCommandName());
        this.originalCommand = command;
        this.plugin = command.getPlugin();

        Set<String> aliasList = command.getAliases();
        if (!aliasList.isEmpty()) {
            setAliases(new ArrayList<>(aliasList));
        }

        String description = command.getDescription();
        if (description != null) {
            setDescription(description);
        }

        String usage = command.getUsage();
        if (usage != null) {
            setUsage(usage);
        }

        this.permission = command.getPermission();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.isEnabled()) {
            return false;
        }

        if (!testPermission(sender)) {
            return true;
        }

        boolean success;

        try {
            success = this.originalCommand.onCommand(sender, this, label, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + label + "' in plugin "
                    + this.plugin.getName(), ex);
        }

        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", label).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return success;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> completions;

        try {
            completions = this.originalCommand.onTabComplete(sender, args);
        } catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
            for (String arg : args) {
                message.append(arg).append(' ');
            }

            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(this.plugin.getName());
            throw new CommandException(message.toString(), ex);
        }

        return completions;
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        if (this.permission == null) {
            return true;
        }

        return this.originalCommand.checkPermission(sender, this.permission, true);
    }

    @Override
    public boolean testPermissionSilent(CommandSender sender) {
        if (this.permission == null) {
            return true;
        }

        return this.originalCommand.checkPermission(sender, this.permission, false);
    }
}
