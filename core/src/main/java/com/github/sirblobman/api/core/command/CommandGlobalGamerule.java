package com.github.sirblobman.api.core.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.utility.MessageUtility;

public class CommandGlobalGamerule extends Command {
    private final CorePlugin plugin;
    public CommandGlobalGamerule(CorePlugin plugin) {
        super(plugin, "global-gamerule");
        this.plugin = plugin;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            World world = getWorld(sender);
            if(world == null) return Collections.emptyList();

            String[] gameRuleArray = world.getGameRules();
            List<String> gameRuleList = Arrays.asList(gameRuleArray);
            return getMatching(gameRuleList, args[0]);
        }

        if(args.length == 2) {
            List<String> valueList = Arrays.asList("true", "false");
            return getMatching(valueList, args[1]);
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if(args.length < 1) return false;

        String gameRule = args[0];
        World senderWorld = getWorld(sender);
        if(!senderWorld.isGameRule(gameRule)) {
            sender.sendMessage(ChatColor.RED + "Unknown Game Rule: " + ChatColor.GRAY + gameRule);
            return true;
        }

        if(args.length < 2) {
            sender.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "Game Rule Values:");
            List<World> worldList = Bukkit.getWorlds();
            for(World world : worldList) {
                String worldName = world.getName();
                String gameRuleValue = world.getGameRuleValue(gameRule);
                sender.sendMessage(ChatColor.GRAY + worldName + ": " + ChatColor.RESET + gameRuleValue);
            }

            return true;
        }

        String value = args[1];
        int successCount = 0;
        int failCount = 0;

        List<World> worldList = Bukkit.getWorlds();
        for(World world : worldList) {
            boolean success = world.setGameRuleValue(gameRule, value);
            if(success) successCount++;
            else failCount++;
        }

        String message1 = MessageUtility.color("&aSuccessfully changed game rule &7" + gameRule + "&a to &7" + value + "&a in " + successCount + " worlds.");
        String message2 = MessageUtility.color("&cFailed to change game rule &7" + gameRule + "&c in " + failCount + " worlds.");
        if(successCount > 1) sender.sendMessage(message1);
        if(failCount > 1) sender.sendMessage(message2);

        return true;
    }

    private World getWorld(CommandSender sender) {
        if(sender instanceof Entity) {
            Entity entitySender = (Entity) sender;
            return entitySender.getWorld();
        }

        if(sender instanceof BlockCommandSender) {
            BlockCommandSender blockSender = (BlockCommandSender) sender;
            Block block = blockSender.getBlock();
            return block.getWorld();
        }

        List<World> worldList = Bukkit.getWorlds();
        return worldList.get(0);
    }
}
