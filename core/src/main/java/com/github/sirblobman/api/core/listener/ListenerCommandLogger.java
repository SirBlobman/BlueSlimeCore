package com.github.sirblobman.api.core.listener;

import java.util.Locale;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.plugin.listener.PluginListener;

public final class ListenerCommandLogger extends PluginListener<CorePlugin> {
    public ListenerCommandLogger(@NotNull CorePlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String command = e.getMessage();
        logCommand(player, command);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(ServerCommandEvent e) {
        CommandSender sender = e.getSender();
        String command = e.getCommand();
        logCommand(sender, command);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(RemoteServerCommandEvent e) {
        CommandSender sender = e.getSender();
        String command = e.getCommand();
        logCommand(sender, command);
    }

    private void logCommand(@NotNull CommandSender sender, @NotNull String command) {
        String senderInfo = getSenderInfo(sender);
        String messageFormat = "Detected command from '%s': '%s'.";
        String logMessage = String.format(Locale.US, messageFormat, senderInfo, command);

        Logger logger = getLogger();
        logger.info(logMessage);
    }

    private String getSenderInfo(@NotNull CommandSender sender) {
        String senderName = sender.getName();
        String senderPrefix = getSenderPrefix(sender);
        return String.format(Locale.US, "%s: %s", senderPrefix, senderName);
    }

    private String getSenderPrefix(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            return "Player";
        }

        if (sender instanceof Entity) {
            return "Entity";
        }

        if (sender instanceof ConsoleCommandSender) {
            return "Console";
        }

        if (sender instanceof BlockCommandSender) {
            BlockCommandSender blockSender = (BlockCommandSender) sender;
            Block block = blockSender.getBlock();
            World world = block.getWorld();

            String worldName = world.getName();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            return String.format(Locale.US, "Block @ (%s, %s, %s, %s)", worldName, x, y, z);
        }

        return "Unknown";
    }
}
