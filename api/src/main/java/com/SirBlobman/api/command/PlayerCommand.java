package com.SirBlobman.api.command;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SirBlobman.api.configuration.ConfigManager;
import com.SirBlobman.api.plugin.SirBlobmanPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerCommand extends CustomCommand {
    public PlayerCommand(SirBlobmanPlugin<?> plugin, String commandName) {
        super(plugin, commandName);
    }
    
    @Override
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
                if(firstParameter != Player.class) continue;
            
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
    public final List<String> onTabComplete(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return Collections.emptyList();
        
        Player player = (Player) sender;
        return onTabComplete(player, args);
    }
    
    @Override
    public final boolean onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            ConfigManager<?> configManager = this.plugin.getConfigManager();
            String message = configManager.getConfigMessage("language.yml", "error.player-only", true);
            sender.sendMessage(message);
            return true;
        }
    
        Player player = (Player) sender;
        return onCommand(player, args);
    }
    
    public abstract List<String> onTabComplete(Player player, String[] args);
    public abstract boolean onCommand(Player player, String[] args);
}