package com.SirBlobman.core.command;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.SirBlobman.api.command.PlayerCommand;
import com.SirBlobman.api.nms.ItemHandler;
import com.SirBlobman.api.nms.MultiVersionHandler;
import com.SirBlobman.api.utility.ItemUtility;
import com.SirBlobman.api.utility.VersionUtility;
import com.SirBlobman.core.CorePlugin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class CommandItemToNBT extends PlayerCommand {
    private final CorePlugin plugin;
    public CommandItemToNBT(CorePlugin plugin) {
        super(plugin, "item-to-nbt");
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return (args.length == 1 ? Collections.singletonList("pretty") : Collections.emptyList());
    }

    @Override
    public boolean execute(Player player, String[] args) {
        ItemStack item = getMainItem(player);
        if(ItemUtility.isAir(item)) {
            player.sendMessage("Air does not have an NBT value.");
            return true;
        }

        MultiVersionHandler multiVersionHandler = this.plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        String nbtString = itemHandler.toNBT(item);
        if(args.length > 0 && args[0].equalsIgnoreCase("pretty")) nbtString = prettyJSON(player, nbtString);

        String[] split = nbtString.split(Pattern.quote("\n"));
        player.sendMessage(split);
        return true;
    }

    @SuppressWarnings("deprecation")
    private ItemStack getMainItem(Player player) {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 9) return player.getItemInHand();

        PlayerInventory playerInventory = player.getInventory();
        return playerInventory.getItemInMainHand();
    }

    private String prettyJSON(Player player, String json) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(json);
        } catch(NoClassDefFoundError | Exception ex) {
            player.sendMessage("Could not parse into pretty JSON, sending normal...");
            return json;
        }
    }
}