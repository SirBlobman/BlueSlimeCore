package com.github.sirblobman.api.utility.paper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

public class CommandData {
    private String name;
    private String usage;
    private String description;
    private String permission;
    private String permissionMessage;

    private CommandExecutor commandExecutor;
    private TabExecutor tabExecutor;

    private final Set<String> aliases;

    public CommandData(@NotNull String commandName, @NotNull CommandExecutor executor) {
        this.name = commandName;
        this.description = null;
        this.usage = null;
        this.commandExecutor = executor;

        if (executor instanceof TabExecutor) {
            this.tabExecutor = (TabExecutor) executor;
        }

        this.aliases = new HashSet<>();
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(@NotNull Collection<String> aliases) {
        this.aliases.clear();
        this.aliases.addAll(aliases);
    }

    public @NotNull CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(@NotNull CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public @Nullable TabExecutor getTabExecutor() {
        return tabExecutor;
    }

    public void setTabExecutor(@Nullable TabExecutor tabExecutor) {
        this.tabExecutor = tabExecutor;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public @Nullable String getUsage() {
        return usage;
    }

    public void setUsage(@Nullable String usage) {
        this.usage = usage;
    }

    public @Nullable String getPermission() {
        return permission;
    }

    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    public @Nullable String getPermissionMessage() {
        return permissionMessage;
    }

    public void setPermissionMessage(@Nullable String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }
}
