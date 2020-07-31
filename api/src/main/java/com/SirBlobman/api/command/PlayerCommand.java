package com.SirBlobman.api.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.api.plugin.ConfigPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PlayerCommand<Plugin extends JavaPlugin & ConfigPlugin> extends CustomCommand<Plugin> {
    public PlayerCommand(Plugin plugin, String commandName) {
        super(plugin, commandName);
    }
    
    @Override
    void registerSubCommands() {
        try {
            Class<?> commandClass = getClass();
            Method[] methodArray = commandClass.getDeclaredMethods();
            for(Method method : methodArray) {
                SubCommand subCommand = method.getDeclaredAnnotation(SubCommand.class);
                if(subCommand == null) continue;
        
                Class<?> returnType = method.getReturnType();
                if(!Boolean.TYPE.equals(returnType)) continue;
        
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length != 2) continue;
        
                Class<?> firstParameter = parameterTypes[0];
                if(!Player.class.equals(firstParameter)) continue;
        
                Class<?> secondParameter = parameterTypes[1];
                if(!String[].class.equals(secondParameter)) continue;
        
                String subCommandName = subCommand.name();
                this.subCommandMap.put(subCommandName, method);
            }
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering sub commands:", ex);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            String message = getConfigMessage("error.player-only", true);
            sender.sendMessage(message);
            return true;
        }
        
        Player player = (Player) sender;
        if(args.length < 1) return onCommand(player, args);
        
        String sub = args[0].toLowerCase();
        String[] newArgs = (args.length < 2 ? new String[0] : Arrays.copyOfRange(args, 1, args.length));
        Method method = this.subCommandMap.getOrDefault(sub, null);
        if(method == null) return onCommand(player, args);
        
        try {
            Object[] parameters = {player, newArgs};
            return (boolean) method.invoke(this, parameters);
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while executing a command:", ex);
            sender.sendMessage(ChatColor.RED + "An error occurred, please tell an admin to check the server console.");
            return true;
        }
    }
    
    @Override
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return Collections.emptyList();
        
        Player player = (Player) sender;
        return onTabComplete(player, args);
    }
    
    @Override
    public final boolean onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            String message = getConfigMessage("error.player-only", true);
            sender.sendMessage(message);
            return true;
        }
    
        Player player = (Player) sender;
        return onCommand(player, args);
    }
    
    public abstract List<String> onTabComplete(Player player, String[] args);
    public abstract boolean onCommand(Player player, String[] args);
}