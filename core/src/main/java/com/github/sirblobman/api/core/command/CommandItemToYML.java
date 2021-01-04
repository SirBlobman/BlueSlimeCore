package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.core.CorePlugin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class CommandItemToYML extends PlayerCommand {
    private final CorePlugin plugin;
    public CommandItemToYML(CorePlugin plugin) {
        super(plugin, "item-to-yml");
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(Player player, String[] args) {
        ItemStack item = getMainItem(player);
        if(ItemUtility.isAir(item)) {
            player.sendMessage("Air does not have a YML value.");
            return true;
        }

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("item", item);
        String configurationString = configuration.saveToString();

        String[] split = configurationString.split(Pattern.quote("\n"));
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
}