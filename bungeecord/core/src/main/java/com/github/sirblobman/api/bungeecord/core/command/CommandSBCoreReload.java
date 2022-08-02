package com.github.sirblobman.api.bungeecord.core.command;

import java.util.Collections;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.utility.Validate;

public final class CommandSBCoreReload extends Command implements TabExecutor {
    private final CorePlugin plugin;

    public CommandSBCoreReload(CorePlugin plugin) {
        super("sbcorereload", "sbcore.reload", "sbcore-reload", "sbc-reload");
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CorePlugin plugin = getPlugin();
        plugin.onReload();

        TextComponent message = new TextComponent("Successfully reloaded the configuration.");
        message.setColor(ChatColor.GREEN);
        sender.sendMessage(message);
    }

    private CorePlugin getPlugin() {
        return this.plugin;
    }
}
