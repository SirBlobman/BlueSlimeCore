package com.github.sirblobman.api.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PlayerCommand extends Command {
    public PlayerCommand(JavaPlugin plugin, String commandName) {
        super(plugin, commandName);
    }

    protected abstract List<String> onTabComplete(Player player, String[] args);
    protected abstract boolean execute(Player player, String[] args);

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            return onTabComplete(player, args);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            return execute(player, args);
        }

        sendMessageOrDefault(sender, "error.player-only", "Only players can execute that command.", null, true);
        return true;
    }
}