package com.github.sirblobman.api.bungeecord.core.command;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;

public final class CommandSBCoreReload extends Command implements TabExecutor {
    private final CorePlugin plugin;

    public CommandSBCoreReload(@NotNull CorePlugin plugin) {
        super("sbcorereload", "sbcore.reload", "sbcore-reload", "sbc-reload");
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return Collections.emptyList();
    }

    @Override
    public void execute(@NotNull CommandSender sender, String @NotNull [] args) {
        CorePlugin plugin = getPlugin();
        plugin.onReload();

        TextComponent message = new TextComponent("Successfully reloaded the configuration.");
        message.setColor(ChatColor.GREEN);
        sender.sendMessage(message);
    }

    private @NotNull CorePlugin getPlugin() {
        return this.plugin;
    }
}
