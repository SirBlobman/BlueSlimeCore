package com.github.sirblobman.api.core.command;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import com.github.sirblobman.api.command.ConsoleCommand;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.language.Replacer;

public final class CommandDebugEvent extends ConsoleCommand {
    public CommandDebugEvent(CorePlugin plugin) {
        super(plugin, "debug-event");
    }
    
    @Override
    public List<String> onTabComplete(ConsoleCommandSender sender, String[] args) {
        if(args.length == 1) {
            Set<String> valueSet = getEnumNames(EventPriority.class);
            return getMatching(args[0], valueSet);
        }
        
        if(args.length == 2) {
            Set<String> valueSet = getExampleEventClasses();
            return getMatching(args[1], valueSet);
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public boolean execute(ConsoleCommandSender sender, String[] args) {
        if(args.length < 2) {
            return false;
        }
        
        String eventPriorityName = args[0].toUpperCase(Locale.US);
        EventPriority eventPriority = matchEnum(EventPriority.class, eventPriorityName);
        if(eventPriority == null) {
            Replacer replacer = message -> message.replace("{value}", eventPriorityName);
            sendMessage(sender, "command.debug-event.invalid-priority", replacer, true);
            return true;
        }
        
        String className = args[1];
        Class<? extends Event> eventClass = getEventClass(className);
        if(eventClass == null) {
            Replacer replacer = message -> message.replace("{value}", className);
            sendMessage(sender, "command.debug-event.invalid-event-class", replacer, true);
            return true;
        }
        
        try {
            HandlerList handlerList = getHandlerList(eventClass);
            logDebugResults(eventClass, handlerList, eventPriority);
            return true;
        } catch(ReflectiveOperationException ex) {
            Logger logger = getLogger();
            sendMessage(sender, "command.debug-event.reflection-error", null, true);
            logger.log(Level.WARNING, "Failed to debug an event because an error occurred:", ex);
            return true;
        }
    }
    
    private Class<? extends Event> getEventClass(String className) {
        try {
            Class<?> namedClass = Class.forName(className);
            return namedClass.asSubclass(Event.class);
        } catch(ReflectiveOperationException ex) {
            return null;
        }
    }
    
    private HandlerList getHandlerList(Class<? extends Event> eventClass) throws ReflectiveOperationException {
        Method method_getHandlerList = eventClass.getMethod("getHandlerList");
        method_getHandlerList.setAccessible(true);
        
        Object object_HandlerList = method_getHandlerList.invoke(null);
        return (HandlerList) object_HandlerList;
    }
    
    private Map<String, Set<String>> getPluginListenerMap(HandlerList handlerList, EventPriority priority) {
        RegisteredListener[] registeredListenerArray = handlerList.getRegisteredListeners();
        Map<String, Set<String>> pluginListenerMap = new HashMap<>();
        
        for(RegisteredListener registeredListener : registeredListenerArray) {
            EventPriority listenerPriority = registeredListener.getPriority();
            if(listenerPriority != priority) {
                continue;
            }
            
            Plugin plugin = registeredListener.getPlugin();
            String pluginName = plugin.getName();
            Set<String> classNameSet = pluginListenerMap.getOrDefault(pluginName, new HashSet<>());
            
            Listener listener = registeredListener.getListener();
            Class<? extends Listener> class_Listener = listener.getClass();
            String className = class_Listener.getName();
            
            classNameSet.add(className);
            pluginListenerMap.putIfAbsent(pluginName, classNameSet);
        }
        
        return pluginListenerMap;
    }
    
    private void logDebugResults(Class<?> eventClass, HandlerList handlerList, EventPriority priority) {
        Map<String, Set<String>> pluginListenerMap = getPluginListenerMap(handlerList, priority);
        Set<Entry<String, Set<String>>> entrySet = pluginListenerMap.entrySet();
        
        String eventClassSimpleName = eventClass.getSimpleName();
        String priorityName = priority.name();
        Replacer replacer = message -> message.replace("{event}", eventClassSimpleName)
                .replace("{priority}", priorityName);
        
        CommandSender console = Bukkit.getConsoleSender();
        String title = getMessage(console, "command.debug-event.results-title", replacer, false);
        
        Logger logger = getLogger();
        logger.info(title);
        
        for(Entry<String, Set<String>> entry : entrySet) {
            String pluginName = entry.getKey();
            logger.info("  " + pluginName + ":");
            
            Set<String> listenerClassNameSet = entry.getValue();
            for(String listenerClassName : listenerClassNameSet) {
                logger.info("  - " + listenerClassName);
            }
        }
    }
    
    private Set<String> getExampleEventClasses() {
        Set<Class<? extends Event>> eventClassSet = new HashSet<>();
        Collections.addAll(eventClassSet, PlayerInteractEvent.class, PlayerDeathEvent.class,
                PlayerTeleportEvent.class, EntitySpawnEvent.class);
        return eventClassSet.stream().map(Class::getName).collect(Collectors.toSet());
    }
}
