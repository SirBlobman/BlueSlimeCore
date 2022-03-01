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
        
        sendMessage(sender, "error.player-only", null, true);
        return true;
    }
    
    /**
     * @param player The {@link Player} that is tab-completing this command.
     * @param args   An array of command arguments.
     * @return The list of tab completions for this combination of sender and command arguments.
     */
    protected abstract List<String> onTabComplete(Player player, String[] args);
    
    /**
     * @param player The {@link Player} that is executing this command.
     * @param args   An array of command arguments.
     * @return {@code true} if the command was executed correctly, {@code false} if the sender needs to see the command
     * usage.
     */
    protected abstract boolean execute(Player player, String[] args);
}
