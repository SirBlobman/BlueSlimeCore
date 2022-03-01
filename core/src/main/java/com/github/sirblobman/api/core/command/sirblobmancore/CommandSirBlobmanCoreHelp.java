package com.github.sirblobman.api.core.command.sirblobmancore;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.utility.MessageUtility;

public class CommandSirBlobmanCoreHelp extends Command {
    public CommandSirBlobmanCoreHelp(CorePlugin plugin) {
        super(plugin, "help");
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        String permissionName = "sirblobman.core.command.sirblobmancore.help";
        if(!checkPermission(sender, permissionName, true)) {
            return true;
        }
        
        String title = MessageUtility.color("&f&lSirBlobmanCore Commands:");
        String[] commandArray = MessageUtility.colorArray(
                "&b/sb help&7: Show this list of commands.",
                "&b/sb reload&7: Reload the configuration files for SirBlobmanCore.",
                "&b/debug-event&7: Show information about which classes are listing to an event (console only)",
                "&b/global-gamerule&7: Change a gamerule for every world on the server.",
                "&b/item-info&7: Show information about the material and damage of an item.",
                "&b/item-to-base64&7: Convert an item to a binary base64 string.",
                "&b/item-to-nbt&7: Convert an item to its NBT format.",
                "&b/item-to-yml&7: Convert an item to a serialized Bukkit YAML format.",
                ""
        );
        
        sender.sendMessage(title);
        sender.sendMessage(commandArray);
        return true;
    }
}
