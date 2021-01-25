package com.github.sirblobman.api.core.listener;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.utility.Validate;

public final class ListenerCommandLogger implements Listener {
    private final CorePlugin plugin;
    public ListenerCommandLogger(CorePlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String command = e.getMessage();
        logCommand(player, command);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onCommand(ServerCommandEvent e) {
        CommandSender sender = e.getSender();
        String command = e.getCommand();
        logCommand(sender, command);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onCommand(RemoteServerCommandEvent e) {
        CommandSender sender = e.getSender();
        String command = e.getCommand();
        logCommand(sender, command);
    }

    private void logCommand(CommandSender sender, String command) {
        String senderInfo = getSenderInfo(sender);
        String message = String.format("Detected command from '%s': '%s'.", senderInfo, command);
        Logger logger = this.plugin.getLogger();

        logger.info(message);
    }

    private String getSenderInfo(CommandSender sender) {
        String senderName = sender.getName();
        return ((sender instanceof Player ? "Player" : "Sender") + ": " + senderName);
    }
}