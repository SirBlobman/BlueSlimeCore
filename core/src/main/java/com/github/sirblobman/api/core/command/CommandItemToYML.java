package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.utility.ItemUtility;

public final class CommandItemToYML extends PlayerCommand {
    public CommandItemToYML(@NotNull CorePlugin plugin) {
        super(plugin, "item-to-yml");
        addAliases("itemtoyml");
        setPermissionName("blue.slime.core.command.item-to-yml");
        setDescription("A debug command to show an item you are holding as a serialized YAML configuration value.");
        setUsage("/<command>");
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull Player player, String @NotNull [] args) {
        ItemStack item = getHeldItem(player);
        if (ItemUtility.isAir(item)) {
            sendMessage(player, "error.invalid-held-item");
            return true;
        }

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("item", item);

        String configurationString = configuration.saveToString();
        String[] split = configurationString.split(Pattern.quote("\n"));
        player.sendMessage(split);
        return true;
    }
}
