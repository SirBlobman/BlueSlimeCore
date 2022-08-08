package com.github.sirblobman.api.core.command.sirblobmancore;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.sirblobman.api.command.Command;
import com.github.sirblobman.api.core.CorePlugin;

public final class SubCommandReload extends Command {
    private final CorePlugin plugin;

    public SubCommandReload(CorePlugin plugin) {
        super(plugin, "reload");
        this.plugin = plugin;

        setPermissionName("sirblobman.core.command.sirblobmancore.reload");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        CorePlugin corePlugin = getCorePlugin();
        corePlugin.reloadConfig();

        sendMessage(sender, "command.sirblobmancore.reload-success", null);
        return true;
    }

    private CorePlugin getCorePlugin() {
        return this.plugin;
    }
}
