package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.VersionUtility;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonElement);
        } catch(NoClassDefFoundError | Exception ex) {
            player.sendMessage("Could not parse into pretty JSON, sending normal...");
            return json;
        }
    }
}