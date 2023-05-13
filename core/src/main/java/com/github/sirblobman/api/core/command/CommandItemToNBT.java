package com.github.sirblobman.api.core.command;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.ItemUtility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public final class CommandItemToNBT extends PlayerCommand {
    private final CorePlugin plugin;
    private final Gson prettyGson;

    public CommandItemToNBT(@NotNull CorePlugin plugin) {
        super(plugin, "item-to-nbt");
        setPermissionName("blue.slime.core.command.item-to-nbt");
        this.plugin = plugin;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        try {
            gsonBuilder.setLenient();
        } catch (NoSuchMethodError ignored) {
            // 1.8 doesn't have the setLenient method in GsonBuilder.
        }

        this.prettyGson = gsonBuilder.create();
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull Player player, String @NotNull [] args) {
        if (args.length == 1) {
            return Collections.singletonList("pretty");
        }

        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull Player player, String @NotNull [] args) {
        ItemStack item = getHeldItem(player);
        if (ItemUtility.isAir(item)) {
            sendMessage(player, "error.invalid-held-item");
            return true;
        }

        CorePlugin plugin = getCorePlugin();
        MultiVersionHandler multiVersionHandler = plugin.getMultiVersionHandler();
        ItemHandler itemHandler = multiVersionHandler.getItemHandler();
        String nbtString = itemHandler.toNBT(item);

        if (args.length > 0 && args[0].equalsIgnoreCase("pretty")) {
            nbtString = prettyJSON(player, nbtString);
        }

        String[] split = nbtString.split(Pattern.quote("\n"));
        player.sendMessage(split);
        return true;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }

    private @NotNull Gson getPrettyGson() {
        return this.prettyGson;
    }

    private @NotNull String prettyJSON(@NotNull Player player, @NotNull String json) {
        try(JsonReader reader = new JsonReader(new StringReader(json))) {
            reader.setLenient(true);

            JsonParser jsonParser = new JsonParser();
            jsonParser.parse(reader);
            JsonElement jsonElement = jsonParser.parse(json);

            Gson prettyGson = getPrettyGson();
            return prettyGson.toJson(jsonElement);
        } catch (NoClassDefFoundError | Exception ex) {
            player.sendMessage("Could not parse into pretty JSON, sending normal...");
            return json;
        }
    }
}
