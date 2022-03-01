package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Replacer;

import org.jetbrains.annotations.Nullable;

public class CommandGlobalGamerule extends Command {
    public CommandGlobalGamerule(CorePlugin plugin) {
        super(plugin, "global-gamerule");
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            World world = getWorld(sender);
            if(world != null) {
                String[] gameRuleArray = world.getGameRules();
                return getMatching(args[0], gameRuleArray);
            }
        }
        
        if(args.length == 2) {
            return getMatching(args[1], "true", "false");
        }
        
        return Collections.emptyList();
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            return false;
        }
        
        World senderWorld = getWorld(sender);
        if(senderWorld == null) {
            sendMessage(sender, "error.world-required", null, true);
            return true;
        }
        
        String gameRuleString = args[0];
        if(!senderWorld.isGameRule(gameRuleString)) {
            Replacer replacer = message -> message.replace("{value}", gameRuleString);
            sendMessage(sender, "command.global-gamerule.invalid-gamerule", replacer, true);
            return true;
        }
        
        if(args.length < 2) {
            showPerWorldValueList(sender, gameRuleString);
            return true;
        }
        
        setGameRule(sender, gameRuleString, args[1]);
        return true;
    }
    
    @Nullable
    private World getWorld(CommandSender sender) {
        Location location = getLocation(sender);
        return location.getWorld();
    }
    
    private void showPerWorldValueList(CommandSender sender, String gameRuleString) {
        Replacer titleReplacer = message -> message.replace("{rule}", gameRuleString);
        sendMessage(sender, "command.global-gamerule.list-title", titleReplacer, true);
        
        List<World> worldList = Bukkit.getWorlds();
        for(World world : worldList) {
            String worldName = world.getName();
            String gameRuleValue = world.getGameRuleValue(gameRuleString);
            Replacer replacer = message -> message.replace("{world}", worldName)
                    .replace("{value}", gameRuleValue);
            sendMessage(sender, "command.global-gamerule.list-line-format", replacer, true);
        }
    }
    
    private void setGameRule(CommandSender sender, String gameRuleString, String value) {
        int successCount = 0;
        int failureCount = 0;
        
        List<World> worldList = Bukkit.getWorlds();
        for(World world : worldList) {
            if(world.setGameRuleValue(gameRuleString, value)) {
                successCount++;
            } else {
                failureCount++;
            }
        }
        
        if(successCount > 0) {
            String finalSuccessCount = Integer.toString(successCount);
            Replacer replacer = message -> message.replace("{count}", finalSuccessCount);
            sendMessage(sender, "command.global-gamerule.success-count", replacer, true);
        }
        
        if(failureCount > 0) {
            String finalFailureCount = Integer.toString(successCount);
            Replacer replacer = message -> message.replace("{count}", finalFailureCount);
            sendMessage(sender, "command.global-gamerule.failure-count", replacer, true);
        }
    }
}
