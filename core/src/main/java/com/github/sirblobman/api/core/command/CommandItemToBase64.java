package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.ItemUtility;

public final class CommandItemToBase64 extends PlayerCommand {
    private final CorePlugin plugin;

    public CommandItemToBase64(@NotNull CorePlugin plugin) {
        super(plugin, "item-to-base64");
        setPermissionName("blue.slime.core.command.item-to-base64");
        this.plugin = plugin;
    }

    @Override
    protected @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(@NotNull Player player, String @NotNull [] args) {
        ItemStack item = getHeldItem(player);
        if (ItemUtility.isAir(item)) {
            sendMessage(player, "error.invalid-held-item");
            return true;
        }

        CorePlugin plugin = getCorePlugin();
        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        String base64String = itemHandler.toBase64String(item);

        player.sendMessage(base64String);
        return true;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }
}
