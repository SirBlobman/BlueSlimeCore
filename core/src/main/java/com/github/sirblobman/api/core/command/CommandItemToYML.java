package com.github.sirblobman.api.core.command;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.command.PlayerCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.utility.ItemUtility;

public final class CommandItemToYML extends PlayerCommand {
    public CommandItemToYML(CorePlugin plugin) {
        super(plugin, "item-to-yml");
    }
    
    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    public boolean execute(Player player, String[] args) {
        ItemStack item = getHeldItem(player);
        if(ItemUtility.isAir(item)) {
            sendMessage(player, "error.invalid-held-item", null, true);
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
