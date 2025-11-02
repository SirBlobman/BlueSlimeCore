package com.github.sirblobman.api.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;
import com.github.sirblobman.api.plugin.ConfigurablePlugin;
import com.github.sirblobman.api.utility.ConfigurationHelper;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.utility.paper.CommandData;
import com.github.sirblobman.api.utility.paper.PaperChecker;
import com.github.sirblobman.api.utility.paper.PaperHelper;
import com.github.sirblobman.api.shaded.adventure.audience.Audience;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public abstract class Command implements TabExecutor {
    private final JavaPlugin plugin;
    private final String commandName;
    private final Map<String, Command> subCommandMap;

    private Permission permission;

    /**
     * @param plugin      The plugin that will be used to register this command.
     * @param commandName The name of the command that will be registered. (must match 'plugin.yml' setting)
     */
    public Command(@NotNull JavaPlugin plugin, @NotNull String commandName) {
        this.plugin = plugin;
        this.commandName = Validate.notEmpty(commandName, "commandName must not be empty!");
        this.subCommandMap = new HashMap<>();
        this.permission = null;
    }

    /**
     * @return The name used for registering this command.
     */
    public final @NotNull String getCommandName() {
        return this.commandName;
    }

    /**
     * @return The plugin used for registering this command.
     */
    protected final @NotNull JavaPlugin getPlugin() {
        return this.plugin;
    }

    protected final @NotNull Logger getLogger() {
        JavaPlugin plugin = getPlugin();
        return plugin.getLogger();
    }

    protected final void printDebug(@NotNull String message) {
        JavaPlugin plugin = getPlugin();
        FileConfiguration configuration = plugin.getConfig();
        if (configuration.getBoolean("debug-mode", false)) {
            Logger logger = getLogger();
            logger.info("[Debug] " + message);
        }
    }

    /**
     * Override this method if your plugin has a {@link LanguageManager} from SirBlobmanCore
     *
     * @return A {@link LanguageManager} or {@code null} if not overridden.
     */
    protected @Nullable LanguageManager getLanguageManager() {
        JavaPlugin plugin = getPlugin();
        if (!(plugin instanceof ConfigurablePlugin)) {
            return null;
        }

        ConfigurablePlugin configurablePlugin = (ConfigurablePlugin) plugin;
        return configurablePlugin.getLanguageManager();
    }

    /**
     * The method used to register this command to the plugin.
     */
    public final void register() {
        String commandName = getCommandName();
        registerCustom(commandName);
    }

    private boolean isPaperPlugin() {
        if (PaperChecker.isPaper() && PaperChecker.hasPaperPluginSupport()) {
            JavaPlugin plugin = getPlugin();
            try (InputStream resource = plugin.getResource("paper-plugin.yml")) {
                return (resource != null);
            } catch (IOException ex) {
                return false;
            }
        }

        return false;
    }

    /**
     * The method used to register this command to the plugin.
     *
     * @param commandName The name to use instead of the actual command name.
     */
    public final void registerCustom(@NotNull String commandName) {
        JavaPlugin plugin = getPlugin();
        if (isPaperPlugin()) {
            printDebug("Detected Paper plugin.");
            printDebug("Attempting command map registration...");

            CommandData commandData = loadCommandData(commandName);
            PaperHelper.registerCommand(plugin, commandData);
            printDebug("Registered command '" + commandName + "' command map method.");
        } else {
            printDebug("Attempting to register with legacy Spigot method first...");

            PluginCommand pluginCommand = plugin.getCommand(commandName);
            if (pluginCommand == null) {
                printDebug("Plugin command '" + commandName + "' is not available.");
                return;
            }

            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
            printDebug("Registered command using classic Spigot method.");
        }
    }

    private @NotNull CommandData loadCommandData(@NotNull String commandName) {
        JavaPlugin plugin = getPlugin();
        CommandData commandData = new CommandData(commandName, this);
        InputStream paperPluginStream = plugin.getResource("paper-plugin.yml");
        InputStream bukkitPluginStream = plugin.getResource("plugin.yml");
        
        YamlConfiguration main = loadConfiguration(paperPluginStream);
        YamlConfiguration backup = loadConfiguration(bukkitPluginStream);

        String basePath = String.format(Locale.US, "commands.%s.", commandName);
        String description = findString(main, backup, basePath + "description");
        String usage = findString(main, backup, basePath + "usage");
        String permission = findString(main, backup, basePath + "permission");
        String permissionMessage = findString(main, backup, basePath + "permission-message");
        List<String> aliases = findStringList(main, backup, basePath + "aliases");

        if (description != null) {
            commandData.setDescription(description);
        }

        if (usage != null) {
            commandData.setUsage(usage);
        }

        if (permission != null) {
            commandData.setPermission(permission);
        }

        if (permissionMessage != null) {
            commandData.setPermissionMessage(permissionMessage);
        }

        if (!aliases.isEmpty()) {
            commandData.setAliases(aliases);
        }

        return commandData;
    }

    private @Nullable YamlConfiguration loadConfiguration(@Nullable InputStream resource) {
        if (resource == null) {
            return null;
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
        return YamlConfiguration.loadConfiguration(reader);
    }

    private @Nullable String findString(@Nullable YamlConfiguration main, @Nullable YamlConfiguration backup, @NotNull String path) {
        if (main != null && main.isSet(path)) {
            return main.getString(path);
        }

        if (backup != null && backup.isSet(path)) {
            return backup.getString(path);
        }

        return null;
    }

    private @NotNull List<String> findStringList(@Nullable YamlConfiguration main, @Nullable YamlConfiguration backup, @NotNull String path) {
        if (main != null && main.isSet(path)) {
            return main.getStringList(path);
        }

        if (backup != null && backup.isSet(path)) {
            return backup.getStringList(path);
        }

        return Collections.emptyList();
    }

    /**
     * Register a command to be a sub-command of this one.
     *
     * @param subCommand The command to register as a sub-command.
     */
    protected final void addSubCommand(@NotNull Command subCommand) {
        String subCommandName = subCommand.getCommandName();
        this.subCommandMap.putIfAbsent(subCommandName, subCommand);
    }

    /**
     * @return An unmodifiable view of the sub commands for this command.
     */
    protected final @NotNull Map<String, Command> getSubCommands() {
        return Collections.unmodifiableMap(this.subCommandMap);
    }

    /**
     * A useful method for tab completion that returns all values in the list that start with the argument. The case of
     * the strings are ignored.
     *
     * @param arg    The argument being used to tab-complete.
     * @param values An iterable of possible values in the tab completion.
     * @return A list of values that match the argument.
     */
    protected final @NotNull List<String> getMatching(@NotNull String arg, @NotNull Iterable<String> values) {
        return MessageUtility.getMatches(arg, values);
    }

    /**
     * A useful method for tab completion that returns all values in the list that start with the argument. The case of
     * the strings are ignored.
     *
     * @param arg    The argument being used to tab-complete.
     * @param values An array of possible values in the tab completion.
     * @return A list of values that match the argument.
     */
    protected final @NotNull List<String> getMatching(@NotNull String arg, String @NotNull ... values) {
        List<String> valueList = Arrays.asList(values);
        return getMatching(arg, valueList);
    }

    /**
     * @param original – the array from which a range is to be copied
     * @param start    – the initial index of the range to be copied, inclusive
     * @return A new {@link String[]} containing all values from start (inclusive) to {@code args.length} (exclusive).
     * If the start is not an index in the original array, an empty array will be returned.
     */
    protected final String @NotNull [] getSubArguments(String @NotNull [] original, int start) {
        if (original.length <= start) {
            return new String[0];
        }

        return Arrays.copyOfRange(original, start, original.length);
    }

    protected final <E extends Enum<E>> @NotNull Set<String> getEnumNames(@NotNull Class<E> enumClass) {
        return ConfigurationHelper.getEnumNames(enumClass);
    }

    protected final <E extends Enum<E>> @Nullable E matchEnum(@NotNull Class<E> enumClass, @NotNull String value) {
        return ConfigurationHelper.parseEnum(enumClass, value, null);
    }

    protected final @NotNull Collection<Player> getOnlinePlayers() {
        Collection<? extends Player> onlinePlayerCollection = Bukkit.getOnlinePlayers();
        return Collections.unmodifiableCollection(onlinePlayerCollection);
    }

    /**
     * @return A set containing the name of each online player as a String.
     */
    protected final @NotNull Set<String> getOnlinePlayerNames() {
        Collection<Player> onlinePlayerCollection = getOnlinePlayers();
        Set<String> playerNameSet = new HashSet<>();

        for (Player player : onlinePlayerCollection) {
            String playerName = player.getName();
            playerNameSet.add(playerName);
        }

        return Collections.unmodifiableSet(playerNameSet);
    }

    /**
     * @param sender The command sender.
     * @return The location of the command sender. Defaults to the main world at 0,0,0 if no location is available.
     * (e.g. console)
     */
    protected final @Nullable Location getLocation(CommandSender sender) {
        if (sender instanceof Entity) {
            Entity entity = (Entity) sender;
            return entity.getLocation();
        }

        if (sender instanceof BlockCommandSender) {
            BlockCommandSender blockSender = (BlockCommandSender) sender;
            Block block = blockSender.getBlock();
            return block.getLocation();
        }

        return null;
    }

    protected final @NotNull Audience getAudience(@NotNull CommandSender commandSender) {
        LanguageManager languageManager = getLanguageManager();
        if (languageManager == null) {
            return Audience.empty();
        }

        Audience audience = languageManager.getAudience(commandSender);
        if (audience == null) {
            return Audience.empty();
        }

        return audience;
    }

    protected final @NotNull Component getMessage(@Nullable CommandSender sender, @NotNull String key,
                                                  Replacer @NotNull... replacerArray) {
        LanguageManager languageManager = getLanguageManager();
        if (languageManager == null) {
            return Component.text(key);
        }

        return languageManager.getMessage(sender, key, replacerArray);
    }

    protected final void sendMessage(@NotNull CommandSender sender, @NotNull String key,
                                     Replacer @NotNull ... replacerArray) {
        LanguageManager languageManager = getLanguageManager();
        if (languageManager == null) {
            return;
        }

        languageManager.sendMessage(sender, key, replacerArray);
    }

    protected final void sendMessageWithPrefix(@NotNull CommandSender sender, @NotNull String key,
                                               Replacer @NotNull... replacerArray) {
        LanguageManager languageManager = getLanguageManager();
        if (languageManager == null) {
            return;
        }

        languageManager.sendMessageWithPrefix(sender, key, replacerArray);
    }

    /**
     * Check if a sender has access to a permission.
     *
     * @param sender         The {@link Permissible} that will be checked, usually a command sender.
     * @param permissionName The name of the permission to check.
     * @param sendMessage    {@code true} if a "no permission" message should be sent, {@code false} for no output.
     * @return {@code true} if the sender has the permission, {@code false} if they do not.
     */
    protected final boolean checkPermission(@NotNull Permissible sender, @NotNull String permissionName,
                                            boolean sendMessage) {
        if (sender.hasPermission(permissionName)) {
            return true;
        }

        if (sendMessage && sender instanceof CommandSender) {
            CommandSender audience = (CommandSender) sender;
            sendNoPermissionMessage(audience, permissionName);
        }

        return false;
    }

    /**
     * Check if a sender has access to a permission.
     *
     * @param sender      The {@link Permissible} that will be checked, usually a command sender.
     * @param permission  The permission to check.
     * @param sendMessage {@code true} if a "no permission" message should be sent, {@code false} for no output.
     * @return {@code true} if the sender has the permission, {@code false} if they do not.
     */
    protected final boolean checkPermission(@NotNull Permissible sender, @NotNull Permission permission,
                                            boolean sendMessage) {
        if (sender.hasPermission(permission)) {
            return true;
        }

        if (sendMessage && sender instanceof CommandSender) {
            CommandSender audience = (CommandSender) sender;
            String permissionName = permission.getName();
            sendNoPermissionMessage(audience, permissionName);
        }

        return false;
    }

    private void sendNoPermissionMessage(@NotNull CommandSender sender, @NotNull String permissionName) {
        Replacer replacer = new StringReplacer("{permission}", permissionName);
        sendMessage(sender, "error.no-permission", replacer);
    }

    protected final @Nullable ItemStack getHeldItem(@NotNull Player player) {
        int minorVersion = VersionUtility.getMinorVersion();
        return (minorVersion < 9 ? getHeldItemLegacy(player) : getHeldItemModern(player));
    }

    @SuppressWarnings("deprecation")
    private @Nullable ItemStack getHeldItemLegacy(@NotNull Player player) {
        return player.getItemInHand();
    }

    private @Nullable ItemStack getHeldItemModern(@NotNull Player player) {
        PlayerInventory playerInventory = player.getInventory();
        return playerInventory.getItemInMainHand();
    }

    /**
     * @param sender The sender who executed the command.
     * @param value  A string value to convert to an integer.
     * @return A valid BigInteger value, or {@code null} if one could not be parsed.
     */
    protected final @Nullable BigInteger parseInteger(@NotNull CommandSender sender, @NotNull String value) {
        try {
            return new BigInteger(value);
        } catch (NumberFormatException ex) {
            Replacer replacer = new StringReplacer("{value}", value);
            sendMessage(sender, "error.invalid-integer", replacer);
            return null;
        }
    }

    /**
     * @param sender The sender who executed the command.
     * @param value  A string value to convert to a decimal.
     * @return A valid BigDecimal value, or {@code null} if one could not be parsed.
     */
    protected final @Nullable BigDecimal parseDecimal(@NotNull CommandSender sender, @NotNull String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            Replacer replacer = new StringReplacer("{value}", value);
            sendMessage(sender, "error.invalid-decimal", replacer);
            return null;
        }
    }

    /**
     * @param sender     The sender of the command.
     * @param targetName The name of the player that should be found.
     * @return an instanceof a {@link Player} if one was found with that name, otherwise {@code null}
     */
    protected final @Nullable Player findTarget(@NotNull CommandSender sender, @NotNull String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target != null) {
            return target;
        }

        Replacer replacer = new StringReplacer("{target}", targetName);
        sendMessage(sender, "error.invalid-target", replacer);
        return null;
    }

    /**
     * Give some items to a player. If their inventory is full, items will be dumped on the ground.
     *
     * @param player    The player that will receive the items.
     * @param itemArray An array of items that will be given.
     */
    protected final void giveItems(@NotNull Player player, ItemStack @NotNull ... itemArray) {
        PlayerInventory playerInventory = player.getInventory();
        Map<Integer, ItemStack> leftover = playerInventory.addItem(itemArray);
        if (leftover.isEmpty()) {
            return;
        }

        sendMessage(player, "error.inventory-full");
        Collection<ItemStack> drops = leftover.values();
        Location location = player.getLocation();
        dropItems(location, drops);
    }

    protected final void dropItems(@NotNull Location location, @NotNull Iterable<ItemStack> drops) {
        World world = location.getWorld();
        Validate.notNull(world, "location must have a valid world!");

        for (ItemStack drop : drops) {
            if (!ItemUtility.isAir(drop)) {
                world.dropItemNaturally(location, drop);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final @NotNull List<String> onTabComplete(@NotNull CommandSender sender,
                                                     @NotNull org.bukkit.command.Command command,
                                                     @NotNull String label, String @NotNull [] args) {
        if (args.length == 0) {
            // Nothing to tab complete.
            return Collections.emptyList();
        }

        List<String> tabCompletionList = new ArrayList<>();

        if (args.length == 1) {
            // Add list of sub commands.
            Map<String, Command> subCommandMap = getSubCommands();
            Set<Entry<String, Command>> subCommandEntrySet = subCommandMap.entrySet();
            for (Entry<String, Command> subCommandEntry : subCommandEntrySet) {
                String subCommandName = subCommandEntry.getKey();
                Command subCommand = subCommandEntry.getValue();

                Permission permission = subCommand.getPermission();
                if (permission != null && !checkPermission(sender, permission, false)) {
                    continue;
                }

                tabCompletionList.add(subCommandName);
            }
        }

        if (args.length > 1) {
            // Use sub command for tab completion.
            String subCommandName = args[0].toLowerCase(Locale.US);
            Map<String, Command> subCommandMap = getSubCommands();
            Command subCommand = subCommandMap.getOrDefault(subCommandName, null);
            if (subCommand != null) {
                Permission permission = subCommand.getPermission();
                if (permission == null || checkPermission(sender, permission, false)) {
                    String[] newArgs = getSubArguments(args, 1);
                    tabCompletionList.addAll(subCommand.onTabComplete(sender, command, label, newArgs));
                }
            }
        }

        // Use all tab completions from current command.
        tabCompletionList.addAll(onTabComplete(sender, args));

        String currentArg = args[args.length - 1];
        return getMatching(currentArg, tabCompletionList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command,
                                   @NotNull String label, String @NotNull [] args) {
        if (args.length >= 1) {
            String subCommandName = args[0].toLowerCase(Locale.US);
            Map<String, Command> subCommandMap = getSubCommands();
            Command subCommand = subCommandMap.getOrDefault(subCommandName, null);
            if (subCommand != null) {
                String[] newArgs = getSubArguments(args, 1);
                return subCommand.onCommand(sender, command, label, newArgs);
            }
        }

        Permission permission = getPermission();
        if (permission != null && !checkPermission(sender, permission, true)) {
            return true;
        }

        return execute(sender, args);
    }

    /**
     * @param sender The {@link CommandSender} that is tab-completing this command.
     * @param args   An array of command arguments.
     * @return The list of tab completions for this combination of sender and command arguments.
     */
    protected abstract @NotNull List<String> onTabComplete(@NotNull CommandSender sender, String @NotNull [] args);

    /**
     * @param sender The {@link CommandSender} that is executing this command.
     * @param args   An array of command arguments.
     * @return {@code true} if the command was executed correctly, {@code false} if the sender needs to see the command
     * usage.
     */
    protected abstract boolean execute(@NotNull CommandSender sender, String @NotNull [] args);

    public @Nullable Permission getPermission() {
        return this.permission;
    }

    public void setPermission(@Nullable Permission permission) {
        this.permission = permission;
    }

    public void setPermissionName(@Nullable String permissionName) {
        if (permissionName == null || permissionName.isEmpty()) {
            setPermission(null);
            return;
        }

        JavaPlugin plugin = getPlugin();
        PluginDescriptionFile pluginDescription = plugin.getDescription();
        List<Permission> permissionList = pluginDescription.getPermissions();
        for (Permission loopPermission : permissionList) {
            String loopPermissionName = loopPermission.getName();
            if (permissionName.equals(loopPermissionName)) {
                setPermission(loopPermission);
                return;
            }
        }

        String pluginName = plugin.getName();
        String commandName = getCommandName();
        String description = ("Permission for plugin '" + pluginName + "' command '/" + commandName + "'.");

        Permission permission = new Permission(permissionName, description, PermissionDefault.OP);
        setPermission(permission);
    }
}
