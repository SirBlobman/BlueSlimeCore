package com.SirBlobman.api.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.api.plugin.SirBlobmanPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.PluginManager;

import org.bukkit.event.Listener;

public abstract class CustomCommand implements TabExecutor {
    final SirBlobmanPlugin<?> plugin;
    final String commandName;
    final Map<String, Method> subCommandMap;
    public CustomCommand(SirBlobmanPlugin<?> plugin, String commandName) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.commandName = Objects.requireNonNull(commandName, "commandName must not be null");
        this.subCommandMap = new HashMap<>();
    }
    
    public String getCommandName() {
        return this.commandName;
    }
    
    public void register() {
        try {
            PluginCommand pluginCommand = this.plugin.getCommand(this.commandName);
            CommandExecutor commandExecutor = pluginCommand.getExecutor();
            if(!this.equals(commandExecutor)) pluginCommand.setExecutor(this);
    
            TabCompleter tabCompleter = pluginCommand.getTabCompleter();
            if(!this.equals(tabCompleter)) pluginCommand.setTabCompleter(this);
            
            if(this instanceof Listener) {
                PluginManager manager = Bukkit.getPluginManager();
                Listener listener = (Listener) this;
                manager.registerEvents(listener, this.plugin);
            }
            
            registerSubCommands();
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering a command:", ex);
        }
    }
    
    void registerSubCommands() {
        try {
            Class<? extends CustomCommand> commandClass = getClass();
            Method[] methodArray = commandClass.getDeclaredMethods();
            for(Method method : methodArray) {
                if(!method.isAccessible()) continue;
    
                Class<?> returnType = method.getReturnType();
                if(returnType != Boolean.TYPE) continue;
    
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length != 2) continue;
                
                Class<?> firstParameter = parameterTypes[0];
                if(firstParameter != CommandSender.class) continue;
                
                Class<?> secondParameter = parameterTypes[1];
                if(secondParameter != String[].class) continue;
    
                SubCommand subCommand = method.getAnnotation(SubCommand.class);
                if(subCommand == null) continue;
                
                String subCommandName = subCommand.name();
                this.subCommandMap.put(subCommandName, method);
            }
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering sub commands:", ex);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return onTabComplete(sender, args);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) return onCommand(sender, args);
        
        String sub = args[0].toLowerCase();
        Method method = this.subCommandMap.getOrDefault(sub, null);
        if(method == null) return onCommand(sender, args);
        
        try {
            Object[] parameters = {sender, args};
            return (boolean) method.invoke(parameters);
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while executing a command:", ex);
            sender.sendMessage(ChatColor.RED + "An error occurred, please tell an admin to check the server console.");
            return true;
        }
    }
    
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
    public abstract boolean onCommand(CommandSender sender, String[] args);
}