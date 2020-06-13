package com.SirBlobman.api.command;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.SirBlobman.api.configuration.ConfigManager;
import com.SirBlobman.api.item.ItemUtil;
import com.SirBlobman.api.plugin.ConfigPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.Listener;

public abstract class CustomCommand<Plugin extends JavaPlugin & ConfigPlugin> implements TabExecutor {
    final String commandName;
    final Map<String, Method> subCommandMap;
    protected final Plugin plugin;
    public CustomCommand(Plugin plugin, String commandName) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        this.commandName = Objects.requireNonNull(commandName, "commandName must not be null");
        this.subCommandMap = new HashMap<>();
    }
    
    public String getCommandName() {
        return this.commandName;
    }
    
    public void register() {
        try {
            PluginCommand pluginCommand = this.plugin.getCommand(this.commandName);
            CommandExecutor commandExecutor = pluginCommand.getExecutor();
            if(!this.equals(commandExecutor)) pluginCommand.setExecutor(this);
    
            TabCompleter tabCompleter = pluginCommand.getTabCompleter();
            if(!this.equals(tabCompleter)) pluginCommand.setTabCompleter(this);
            
            if(this instanceof Listener) {
                PluginManager manager = Bukkit.getPluginManager();
                Listener listener = (Listener) this;
                manager.registerEvents(listener, this.plugin);
            }
            
            Logger logger = this.plugin.getLogger();
            logger.info("Registered command '/" + this.commandName + "'.");
            registerSubCommands();
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering a command:", ex);
        }
    }
    
    void registerSubCommands() {
        try {
            Class<?> commandClass = getClass();
            Method[] methodArray = commandClass.getDeclaredMethods();
            for(Method method : methodArray) {
                SubCommand subCommand = method.getDeclaredAnnotation(SubCommand.class);
                if(subCommand == null) continue;
        
                Class<?> returnType = method.getReturnType();
                if(!Boolean.TYPE.equals(returnType)) continue;
        
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length != 2) continue;
        
                Class<?> firstParameter = parameterTypes[0];
                if(!CommandSender.class.equals(firstParameter)) continue;
        
                Class<?> secondParameter = parameterTypes[1];
                if(!String[].class.equals(secondParameter)) continue;
        
                String subCommandName = subCommand.name();
                this.subCommandMap.put(subCommandName, method);
            }
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while registering sub commands:", ex);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return onTabComplete(sender, args);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) return onCommand(sender, args);
        
        String sub = args[0].toLowerCase();
        String[] newArgs = (args.length < 2 ? new String[0] : Arrays.copyOfRange(args, 1, args.length));
        Method method = this.subCommandMap.getOrDefault(sub, null);
        if(method == null) return onCommand(sender, args);
        
        try {
            Object[] parameters = {sender, newArgs};
            return (boolean) method.invoke(this, parameters);
        } catch(Exception ex) {
            Logger logger = this.plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while executing a command:", ex);
            sender.sendMessage(ChatColor.RED + "An error occurred, please tell an admin to check the server console.");
            return true;
        }
    }
    
    public final List<String> getMatching(Iterable<String> valueList, String arg) {
        if(valueList == null || arg == null) return Collections.emptyList();
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
        Collection<? extends Player> onlinePlayerList = Bukkit.getOnlinePlayers();
        return onlinePlayerList.stream().map(Player::getName).collect(Collectors.toSet());
    }
    
    public final String getConfigMessage(String path, boolean translateColorCodes) {
        ConfigManager<?> configManager = this.plugin.getConfigManager();
        return configManager.getConfigMessage("language.yml", path, translateColorCodes);
    }
    
    public final BigInteger parseInteger(CommandSender sender, String string) {
        try {
            return new BigInteger(string);
        } catch(NumberFormatException ex) {
            String message = getConfigMessage("error.invalid-number", true).replace("{value}", string);
            sender.sendMessage(message);
            return null;
        }
    }
    
    public final BigDecimal parseDecimal(CommandSender sender, String string) {
        try {
            return new BigDecimal(string);
        } catch(NumberFormatException ex) {
            String message = getConfigMessage("error.invalid-number", true).replace("{value}", string);
            sender.sendMessage(message);
            return null;
        }
    }
    
    public final Player getTarget(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if(target != null) return target;
        
        String message = getConfigMessage("error.invalid-target", true).replace("{target}", targetName);
        sender.sendMessage(message);
        return null;
    }
    
    public final void giveItems(Player player, ItemStack... itemArray) {
        PlayerInventory playerInventory = player.getInventory();
        Map<Integer, ItemStack> leftover = playerInventory.addItem(itemArray);
        if(leftover.isEmpty()) return;
        
        String message = getConfigMessage("error.inventory-full", true);
        player.sendMessage(message);
        
        World world = player.getWorld();
        Location location = player.getLocation();
        Collection<ItemStack> dropCollection = leftover.values();
        
        for(ItemStack drop : dropCollection) {
            if(ItemUtil.isAir(drop)) continue;
            world.dropItem(location, drop);
        }
    }
    
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
    public abstract boolean onCommand(CommandSender sender, String[] args);
}