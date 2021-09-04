package com.github.sirblobman.api.command;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.bukkit.util.StringUtil;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.Nullable;

public abstract class Command implements TabExecutor {
    private final JavaPlugin plugin;
    private final String commandName;
    private final Map<String, Command> subCommandMap;

    /**
     * @param plugin The plugin that will be used to register this command.
     * @param commandName The name of the command that will be registered. (must match 'plugin.yml' setting)
     */
    public Command(JavaPlugin plugin, String commandName) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.commandName = Validate.notEmpty(commandName, "commandName cannot be empty or null!");
        this.subCommandMap = new HashMap<>();
    }

    /**
     * @return The name used for registering this command.
     */
    public final String getCommandName() {
        return this.commandName;
    }

    /**
     * @return The plugin used for registering this command.
     */
    protected final JavaPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * Override this method if your plugin has a {@link LanguageManager} from SirBlobmanCore
     * @return A {@link LanguageManager} or {@code null} if not overridden.
     */
    @Nullable
    protected LanguageManager getLanguageManager() {
        JavaPlugin plugin = getPlugin();
        if(!(plugin instanceof ConfigurablePlugin)) return null;

        ConfigurablePlugin configurablePlugin = (ConfigurablePlugin) plugin;
        return configurablePlugin.getLanguageManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label,
                                            String[] args) {
        List<String> tabCompletions = new ArrayList<>(onTabComplete(sender, args));
        if(args.length == 1) {
            tabCompletions.addAll(onTabComplete(sender, args));
            return getMatching(tabCompletions, args[0]);
        }
        
        if(args.length > 1) {
            String subCommandName = args[0].toLowerCase(Locale.US);
            Map<String, Command> subCommands = getSubCommands();
            Command subCommand = subCommands.getOrDefault(subCommandName, null);
            if(subCommand != null) {
                String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                return subCommand.onTabComplete(sender, command, label, newArgs);
            }
        }
        
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label,
                                   String[] args) {
        if(args.length >= 1) {
            String subCommandName = args[0].toLowerCase(Locale.US);
            Map<String, Command> subCommands = getSubCommands();
            Command subCommand = subCommands.getOrDefault(subCommandName, null);
            if(subCommand != null) {
                String[] newArgs = (args.length < 2 ? new String[0] :
                        Arrays.copyOfRange(args, 1, args.length));
                return subCommand.onCommand(sender, command, label, newArgs);
            }
        }
        
        return execute(sender, args);
    }

    /**
     * The method used to register this command to the plugin.
     */
    public final void register() {
        JavaPlugin plugin = getPlugin();
        String commandName = getCommandName();

        try {
            PluginCommand pluginCommand = plugin.getCommand(commandName);
            if(pluginCommand == null) {
                Logger logger = plugin.getLogger();
                logger.log(Level.WARNING,
                        "The command '" + commandName + "' could not be registered because an error occurred:");
                logger.warning("  Command '" + commandName + "' is missing in plugin.yml");
                return;
            }

            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        } catch(Exception ex) {
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING,
                    "An error occurred while registering the command '/" + commandName + "':", ex);
        }
    }
    
    /**
     * Register a command to be a sub-command of this one.
     * @param subCommand The command to register as a sub-command.
     */
    protected final void addSubCommand(Command subCommand) {
        Validate.notNull(subCommand, "subCommand must not be null!");
        String subCommandName = subCommand.getCommandName();
        this.subCommandMap.putIfAbsent(subCommandName, subCommand);
    }
    
    /**
     * @return An unmodifiable view of the sub commands for this command.
     */
    protected final Map<String, Command> getSubCommands() {
        return Collections.unmodifiableMap(this.subCommandMap);
    }

    /**
     * A useful method for tab completion that returns all values in the list that start with the argument.
     * The case of the strings are ignored.
     * @param valueList A list of possible values in the tab completion.
     * @param arg The argument being used to tab-complete.
     * @return A list of values that match the argument.
     */
    protected final List<String> getMatching(Iterable<String> valueList, String arg) {
        List<String> matchList = new ArrayList<>();
        return StringUtil.copyPartialMatches(arg, valueList, matchList);
    }

    /**
     * @return A list of names for every online player.
     */
    protected final Set<String> getOnlinePlayerNames() {
        Set<String> nameSet = new HashSet<>();
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();

        for(Player player : onlinePlayerCollection) {
            String playerName = player.getName();
            nameSet.add(playerName);
        }

        return nameSet;
    }

    /**
     * Send a translatable message to a {@link CommandSender}.
     * @param sender The sender of the command that will receive the message.
     * @param key The message key in the language configuration.
     * @param defaultMessage The default message if one is not found in the configuration.
     * @param replacer {@code null}, or a replacer for variables in the message.
     * @param color {@code true} to apply bukkit color codes, {@code false} to not change the message.
     */
    protected final void sendMessageOrDefault(CommandSender sender, String key, String defaultMessage,
                                              Replacer replacer, boolean color) {
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

    /**
     * Check if a sender has access to a permission.
     * @param sender The command sender who will be checked.
     * @param permission The name of the permission to check for.
     * @param sendMessage {@code true} if a "no permission" message should be sent, {@code false} for no output.
     * @return {@code true} if the sender has the permission, {@code false} if they do not.
     */
    protected final boolean checkPermission(CommandSender sender, String permission, boolean sendMessage) {
        boolean hasPermission = sender.hasPermission(permission);
        if(!hasPermission && sendMessage) {
            Replacer replacer = message -> message.replace("{permission}", permission);
            sendMessageOrDefault(sender, "error.no-permission", "Missing Permission: {permission}",
                    replacer, true);
        }

        return hasPermission;
    }

    /**
     * @param sender The sender who executed the command.
     * @param value A string value to convert to an integer.
     * @return A valid BigInteger value, or {@code null} if one could not be parsed.
     */
    @Nullable
    protected final BigInteger parseInteger(CommandSender sender, String value) {
        try {
            return new BigInteger(value);
        } catch(NumberFormatException ex) {
            Replacer replacer = message -> message.replace("{value}", value);
            sendMessageOrDefault(sender, "error.invalid-integer", "Unknown Integer: {value}",
                    replacer, true);
            return null;
        }
    }

    /**
     * @param sender The sender who executed the command.
     * @param value A string value to convert to a decimal.
     * @return A valid BigDecimal value, or {@code null} if one could not be parsed.
     */
    @Nullable
    protected final BigDecimal parseDecimal(CommandSender sender, String value) {
        try {
            return new BigDecimal(value);
        } catch(NumberFormatException ex) {
            Replacer replacer = message -> message.replace("{value}", value);
            sendMessageOrDefault(sender, "error.invalid-decimal", "Unknown Decimal: {value}",
                    replacer, true);
            return null;
        }
    }

    /**
     * @param sender The sender of the command.
     * @param targetName The name of the player that should be found.
     * @return an instanceof a {@link Player} if one was found with that name, otherwise {@code null}
     */
    @Nullable
    protected final Player findTarget(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if(target != null) return target;

        Replacer replacer = message -> message.replace("{target}", targetName);
        sendMessageOrDefault(sender, "error.invalid-target", "Unknown Player: {target}",
                replacer, true);
        return null;
    }

    /**
     * Give some items to a player. If their inventory is full, items will be dumped on the ground.
     * @param player The player that will receive the items.
     * @param itemArray An array of items that will be given.
     */
    protected final void giveItems(Player player, ItemStack... itemArray) {
        PlayerInventory playerInventory = player.getInventory();
        Map<Integer, ItemStack> leftover = playerInventory.addItem(itemArray);
        if(leftover.isEmpty()) return;

        World world = player.getWorld();
        Location location = player.getLocation();
        sendMessageOrDefault(player, "error.inventory-full", "Inventory Full!",
                null, true);

        Collection<ItemStack> dropCollection = leftover.values();
        for(ItemStack item : dropCollection) {
            if(ItemUtility.isAir(item)) continue;
            world.dropItemNaturally(location, item);
        }
    }

    /**
     * @param sender The {@link CommandSender} that is tab-completing this command.
     * @param args An array of command arguments.
     * @return The list of tab completions for this combination of sender and command arguments.
     */
    protected abstract List<String> onTabComplete(CommandSender sender, String[] args);

    /**
     * @param sender The {@link CommandSender} that is executing this command.
     * @param args An array of command arguments.
     * @return {@code true} if the command was executed correctly, {@code false} if the sender needs to see the command usage.
     */
    protected abstract boolean execute(CommandSender sender, String[] args);
}
