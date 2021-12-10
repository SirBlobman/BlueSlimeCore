package com.github.sirblobman.api.core.command.sirblobmancore;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;

// TODO
public class CommandSirBlobmanCoreReload extends Command {
    public CommandSirBlobmanCoreReload(CorePlugin plugin) {
        super(plugin, "reload");
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        return false;
    }
}
