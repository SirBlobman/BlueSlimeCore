package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.replacer.IntegerReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;

import org.jetbrains.annotations.Nullable;

public final class CommandGlobalGamerule extends Command {
    public CommandGlobalGamerule(CorePlugin plugin) {
        super(plugin, "global-gamerule");
        setPermissionName("blue.slime.core.command.global-gamerule");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            World world = getWorld(sender);
            if (world == null) {
                return Collections.emptyList();
            }

            String[] gameRuleArray = world.getGameRules();
            return getMatching(args[0], gameRuleArray);
        }

        if (args.length == 2) {
            return getMatching(args[1], "true", "false");
        }

        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }

        World senderWorld = getWorld(sender);
        if (senderWorld == null) {
            sendMessage(sender, "error.world-required");
            return true;
        }

        String gameRuleString = args[0];
        if (!senderWorld.isGameRule(gameRuleString)) {
            Replacer replacer = new StringReplacer("{value}", gameRuleString);
            sendMessage(sender, "command.global-gamerule.invalid-gamerule", replacer);
            return true;
        }

        if (args.length < 2) {
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
        Replacer titleReplacer = new StringReplacer("{rule}", gameRuleString);
        sendMessage(sender, "command.global-gamerule.list-title", titleReplacer);

        List<World> worldList = Bukkit.getWorlds();
        for (World world : worldList) {
            String worldName = world.getName();
            String gameRuleValue = world.getGameRuleValue(gameRuleString);

            Replacer worldReplacer = new StringReplacer("{world}", worldName);
            Replacer valueReplacer = new StringReplacer("{value}", gameRuleValue);
            sendMessage(sender, "command.global-gamerule.list-line-format", worldReplacer, valueReplacer);
        }
    }

    private void setGameRule(CommandSender sender, String gameRuleString, String value) {
        int successCount = 0;
        int failureCount = 0;

        List<World> worldList = Bukkit.getWorlds();
        for (World world : worldList) {
            if (world.setGameRuleValue(gameRuleString, value)) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        if (successCount > 0) {
            Replacer countReplacer = new IntegerReplacer("{count}", successCount);
            Replacer ruleReplacer = new StringReplacer("{rule}", gameRuleString);
            Replacer valueReplacer = new StringReplacer("{value}", value);
            sendMessage(sender, "command.global-gamerule.success-count", countReplacer, ruleReplacer,
                    valueReplacer);
        }

        if (failureCount > 0) {
            Replacer countReplacer = new IntegerReplacer("{count}", failureCount);
            Replacer ruleReplacer = new StringReplacer("{rule}", gameRuleString);
            Replacer valueReplacer = new StringReplacer("{value}", value);
            sendMessage(sender, "command.global-gamerule.failure-count", countReplacer, ruleReplacer,
                    valueReplacer);
        }
    }
}
