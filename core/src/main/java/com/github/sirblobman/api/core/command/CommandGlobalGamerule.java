package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.replacer.IntegerReplacer;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;

public final class CommandGlobalGamerule extends Command {
    public CommandGlobalGamerule(@NotNull CorePlugin plugin) {
        super(plugin, "global-gamerule");
        addAliases("globalgamerule");
        setPermissionName("blue.slime.core.command.global-gamerule");
        setDescription("Change a game rule for every world on your server.");
        setUsage("/<command> <rule> [value]");
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
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
    protected boolean execute(@NotNull CommandSender sender, String @NotNull [] args) {
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

    private @Nullable World getWorld(@NotNull CommandSender sender) {
        Location location = getLocation(sender);
        if (location == null) {
            return null;
        }

        return location.getWorld();
    }

    private void showPerWorldValueList(@NotNull CommandSender sender, @NotNull String gameRuleName) {
        Replacer ruleReplacer = new StringReplacer("{rule}", gameRuleName);
        sendMessage(sender, "command.global-gamerule.list-title", ruleReplacer);

        List<World> worldList = Bukkit.getWorlds();
        for (World world : worldList) {
            String worldName = world.getName();
            String gameRuleValue = world.getGameRuleValue(gameRuleName);

            Replacer worldReplacer = new StringReplacer("{world}", worldName);
            Replacer valueReplacer = new StringReplacer("{value}", gameRuleValue);
            sendMessage(sender, "command.global-gamerule.list-line-format", worldReplacer, valueReplacer);
        }
    }

    private void setGameRule(@NotNull CommandSender sender, @NotNull String gameRuleName, @NotNull String value) {
        int successCount = 0;
        int failureCount = 0;

        List<World> worldList = Bukkit.getWorlds();
        for (World world : worldList) {
            if (world.setGameRuleValue(gameRuleName, value)) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        Replacer ruleReplacer = new StringReplacer("{rule}", gameRuleName);
        Replacer valueReplacer = new StringReplacer("{value}", value);

        if (successCount > 0) {
            String key = "command.global-gamerule.success-count";
            Replacer countReplacer = new IntegerReplacer("{count}", successCount);
            sendMessage(sender, key, countReplacer, ruleReplacer, valueReplacer);
        }

        if (failureCount > 0) {
            String key = "command.global-gamerule.failure-count";
            Replacer countReplacer = new IntegerReplacer("{count}", failureCount);
            sendMessage(sender, key, countReplacer, ruleReplacer, valueReplacer);
        }
    }
}
