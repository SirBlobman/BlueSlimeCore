package com.github.sirblobman.api.command;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.Nullable;

public abstract class Command implements TabExecutor {
    private final JavaPlugin plugin;
    private final String commandName;
    public Command(JavaPlugin plugin, String commandName) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.commandName = Validate.notEmpty(commandName, "commandName cannot be empty or null!");
    }

    protected abstract List<String> onTabComplete(CommandSender sender, String[] args);
    protected abstract boolean execute(CommandSender sender, String[] args);

    protected final JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Nullable
    protected LanguageManager getLanguageManager() {
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
        String commandName = getCommandName();
        try {
            PluginCommand pluginCommand = this.plugin.getCommand(commandName);
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering the command '" + commandName + "':", ex);
        }
    }

    protected final List<String> getMatching(Iterable<String> valueList, String arg) {
        List<String> matchList = new ArrayList<>();
        String lowerArg = arg.toLowerCase();

        for(String value : valueList) {
            String lowerValue = value.toLowerCase();
            if(!lowerValue.startsWith(lowerArg)) continue;
            matchList.add(value);
        }

        return matchList;
    }

    protected final Set<String> getOnlinePlayerNames() {
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        return onlinePlayerCollection.stream().map(Player::getName).collect(Collectors.toSet());
    }

    protected final void sendMessageOrDefault(CommandSender sender, String key, String defaultMessage, Replacer replacer, boolean color) {
        LanguageManager languageManager = getLanguageManager();
        if(languageManager != null) {
            languageManager.sendMessage(sender, key, replacer, color);
            return;
        }

        if(defaultMessage != null && !defaultMessage.isEmpty()) {
            String colored = (color ? MessageUtility.color(defaultMessage) : defaultMessage);
            String replaced = (replacer == null ? colored : replacer.replace(colored));
            sender.sendMessage(replaced);
        }
    }

    protected final boolean checkPermission(CommandSender sender, String permission, boolean sendMessage) {
        boolean hasPermission = sender.hasPermission(permission);
        if(!hasPermission && sendMessage) {
            Replacer replacer = message -> message.replace("{permission}", permission);
            sendMessageOrDefault(sender, "error.no-permission", "Missing Permission: {permission}", replacer, true);
        }

        return hasPermission;
    }

    protected final BigInteger parseInteger(CommandSender sender, String value) {
        try {
            return new BigInteger(value);
        } catch(NumberFormatException ex) {
            Replacer replacer = message -> message.replace("{value}", value);
            sendMessageOrDefault(sender, "error.invalid-integer", "Unknown Integer: {value}", replacer, true);
            return null;
        }
    }

    protected final BigDecimal parseDecimal(CommandSender sender, String value) {
        try {
            return new BigDecimal(value);
        } catch(NumberFormatException ex) {
            Replacer replacer = message -> message.replace("{value}", value);
            sendMessageOrDefault(sender, "error.invalid-decimal", "Unknown Decimal: {value}", replacer, true);
            return null;
        }
    }

    protected final Player findTarget(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if(target != null) return target;

        Replacer replacer = message -> message.replace("{target}", targetName);
        sendMessageOrDefault(sender, "error.invalid-target", "Unknown Player: {target}", replacer, true);
        return null;
    }

    protected final void giveItems(Player player, ItemStack... itemArray) {
        PlayerInventory playerInventory = player.getInventory();
        Map<Integer, ItemStack> leftover = playerInventory.addItem(itemArray);
        if(leftover.isEmpty()) return;

        World world = player.getWorld();
        Location location = player.getLocation();
        sendMessageOrDefault(player, "error.inventory-full", "Inventory Full!", null, true);

        Collection<ItemStack> dropCollection = leftover.values();
        for(ItemStack item : dropCollection) {
            if(ItemUtility.isAir(item)) continue;
            world.dropItemNaturally(location, item);
        }
    }
}