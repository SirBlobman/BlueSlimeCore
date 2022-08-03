package com.github.sirblobman.api.bungeecord.core.command;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurationManager;
import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;

public final class CommandSBCoreHide extends Command implements TabExecutor {
    private final CorePlugin plugin;

    public CommandSBCoreHide(CorePlugin plugin) {
        super("sbcorehide", "sbcore.hide", "sbcore-hide", "sbchide");
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Set<String> valueSet = Set.of("on", "off");
            return MessageUtility.getMatches(args[0], valueSet);
        }

        return Collections.emptyList();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            TextComponent message = new TextComponent("Only players can execute this command.");
            message.setColor(ChatColor.RED);
            sender.sendMessage(message);
            return;
        }

        if (args.length >= 1) {
            String toggle = args[0];
            switch (toggle) {
                case "on" -> setHidden(player, true);
                case "off" -> setHidden(player, false);
                default -> {
                    TextComponent message = new TextComponent("/sbcorehide [on/off]");
                    message.setColor(ChatColor.RED);
                    sender.sendMessage(message);
                    return;
                }
            }
        } else {
            boolean hidden = isHidden(player);
            setHidden(player, !hidden);
        }

        boolean hidden = isHidden(player);
        TextComponent message1 = new TextComponent("Hide Status: ");
        message1.setColor(ChatColor.GREEN);

        TextComponent message2 = new TextComponent(hidden ? "ON" : "OFF");
        message2.setColor(hidden ? ChatColor.GREEN : ChatColor.RED);

        message1.addExtra(message2);
        player.sendMessage(message1);
    }

    private CorePlugin getPlugin() {
        return this.plugin;
    }

    private Configuration getConfiguration() {
        CorePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        return configurationManager.get("hidden.yml");
    }

    private void saveConfiguration() {
        CorePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        configurationManager.save("hidden.yml");
    }

    private boolean isHidden(ProxiedPlayer player) {
        UUID playerId = player.getUniqueId();
        String playerIdString = playerId.toString();

        Configuration configuration = getConfiguration();
        return configuration.getBoolean(playerIdString, false);
    }

    private void setHidden(ProxiedPlayer player, boolean hidden) {
        UUID playerId = player.getUniqueId();
        String playerIdString = playerId.toString();

        Configuration configuration = getConfiguration();
        configuration.set(playerIdString, hidden);
        saveConfiguration();
    }
}
