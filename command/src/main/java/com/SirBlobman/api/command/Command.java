package com.SirBlobman.api.command;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.language.LanguageManager;
import com.SirBlobman.api.language.Replacer;
import com.SirBlobman.api.utility.Validate;

public abstract class Command implements TabExecutor {
    private final JavaPlugin plugin;
    private final String commandName;
    public Command(JavaPlugin plugin, String commandName) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.commandName = Validate.notEmpty(commandName, "commandName cannot be empty or null!");
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
    public abstract boolean execute(CommandSender sender, String[] args);
    public LanguageManager getLanguageManager() {
        return null;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return onTabComplete(sender, args);
    }

    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return execute(sender, args);
    }

    public final String getCommandName() {
        return this.commandName;
    }

    public final void register() {
        try {
            PluginCommand pluginCommand = this.plugin.getCommand(this.commandName);
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering the '" + commandName + "' command:", ex);
        }
    }

    public final List<String> getMatching(Iterable<String> valueList, String arg) {
        List<String> matchList = new ArrayList<>();
        String lowerArg = arg.toLowerCase();
        for(String value : valueList) {
            String lowerValue = value.toLowerCase();
            if(!lowerValue.startsWith(lowerArg)) continue;
            matchList.add(value);
        }
        return matchList;
    }

    public final Set<String> getOnlinePlayerNames() {
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        return onlinePlayerCollection.stream().map(Player::getName).collect(Collectors.toSet());
    }

    public final void sendMessageOrDefault(CommandSender sender, String key, String defaultMessage, Replacer replacer, boolean color) {
        LanguageManager languageManager = getLanguageManager();
        if(languageManager == null) {
            String replaced = (replacer == null ? defaultMessage : replacer.replace(defaultMessage));
            sender.sendMessage(replaced);
            return;
        }

        languageManager.sendMessage(sender, key, replacer, color);
    }

    public final boolean checkPermission(CommandSender sender, String permission, boolean sendMessage) {
        boolean hasPermission = sender.hasPermission(permission);
        if(!hasPermission && sendMessage) {
            Replacer replacer = message -> message.replace("{permission}", permission);
            sendMessageOrDefault(sender, "error.no-permission", "You do not have access to that feature.", replacer, true);
        }

        return hasPermission;
    }

    public final BigInteger parseInteger(CommandSender sender, String value) {
        try {
            return new BigInteger(value);
        } catch(NumberFormatException ex) {
            LanguageManager languageManager = getLanguageManager();
            if(languageManager != null) {
                Replacer replacer = message -> message.replace("{value}", value);
                languageManager.sendMessage(sender, "error.invalid-integer", replacer, true);
                return null;
            }

            String message = ("Unknown Integer: " + value);
            sender.sendMessage(message);
            return null;
        }
    }

    public final BigDecimal parseDecimal(CommandSender sender, String value) {
        try {
            return new BigDecimal(value);
        } catch(NumberFormatException ex) {
            LanguageManager languageManager = getLanguageManager();
            if(languageManager != null) {
                Replacer replacer = message -> message.replace("{value}", value);
                languageManager.sendMessage(sender, "error.invalid-decimal", replacer, true);
                return null;
            }

            String message = ("Unknown Decimal: " + value);
            sender.sendMessage(message);
            return null;
        }
    }

    public final Player findTarget(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if(target != null) return target;

        LanguageManager languageManager = getLanguageManager();
        if(languageManager != null) {
            Replacer replacer = message -> message.replace("{target}", targetName);
            languageManager.sendMessage(sender, "error.invalid-target", replacer, true);
            return null;
        }

        String message = ("Unknown Player: " + targetName);
        sender.sendMessage(message);
        return null;
    }

    public final void giveItems(Player player, ItemStack... itemArray) {
        PlayerInventory playerInventory = player.getInventory();
        HashMap<Integer, ItemStack> leftover = playerInventory.addItem(itemArray);
        if(leftover.isEmpty()) return;

        LanguageManager languageManager = getLanguageManager();
        if(languageManager != null) {
            languageManager.sendMessage(player, "error.inventory-full", null, true);
        } else {
            player.sendMessage("Inventory Full!");
        }

        World world = player.getWorld();
        Location location = player.getLocation();
        Collection<ItemStack> dropCollection = leftover.values();
        for(ItemStack drop : dropCollection) {
            if(drop == null) continue;

            Material type = drop.getType();
            if(type == Material.AIR) continue;

            world.dropItemNaturally(location, drop);
        }
    }
}