package com.github.sirblobman.api.core.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
import com.github.sirblobman.api.language.replacer.Replacer;
import com.github.sirblobman.api.language.replacer.StringReplacer;

public final class CommandDebugEvent extends ConsoleCommand {
    public CommandDebugEvent(@NotNull CorePlugin plugin) {
        super(plugin, "debug-event");
        addAliases("debug-event");
        setPermissionName("blue.slime.core.command.debug-event");
        setDescription("Show information about handlers for an event class.");
        setUsage("/<command> <priority> <full.package.with.ClassNameEvent>");
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull ConsoleCommandSender sender, String @NotNull [] args) {
        if (args.length == 1) {
            Set<String> valueSet = getEnumNames(EventPriority.class);
            return getMatching(args[0], valueSet);
        }

        if (args.length == 2) {
            Set<String> valueSet = getExampleEventClasses();
            return getMatching(args[1], valueSet);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull ConsoleCommandSender sender, String @NotNull [] args) {
        if (args.length < 2) {
            return false;
        }

        String eventPriorityName = args[0];
        EventPriority eventPriority = matchEnum(EventPriority.class, eventPriorityName);
        if (eventPriority == null) {
            Replacer replacer = new StringReplacer("{value}", eventPriorityName);
            sendMessage(sender, "command.debug-event.invalid-priority", replacer);
            return true;
        }

        String className = args[1];
        Class<? extends Event> eventClass = getEventClass(className);
        if (eventClass == null) {
            Replacer replacer = new StringReplacer("{value}", className);
            sendMessage(sender, "command.debug-event.invalid-event-class", replacer);
            return true;
        }

        try {
            HandlerList handlerList = getHandlerList(eventClass);
            logDebugResults(eventClass, handlerList, eventPriority);
            return true;
        } catch (ReflectiveOperationException ex) {
            Logger logger = getLogger();
            sendMessage(sender, "command.debug-event.reflection-error");
            logger.log(Level.WARNING, "Failed to debug an event because an error occurred:", ex);
            return true;
        }
    }

    private @Nullable Class<? extends Event> getEventClass(@NotNull String className) {
        try {
            Class<?> namedClass = Class.forName(className);
            return namedClass.asSubclass(Event.class);
        } catch (ReflectiveOperationException | ClassCastException ex) {
            return null;
        }
    }

    private @NotNull HandlerList getHandlerList(@NotNull Class<? extends Event> eventClass)
            throws ReflectiveOperationException {
        Method method_getHandlerList = eventClass.getMethod("getHandlerList");
        method_getHandlerList.setAccessible(true);

        Object object_HandlerList = method_getHandlerList.invoke(null);
        return (HandlerList) object_HandlerList;
    }

    private @NotNull Map<String, Set<String>> getPluginListenerMap(@NotNull HandlerList handlerList,
                                                                   @NotNull EventPriority priority) {
        RegisteredListener[] registeredListenerArray = handlerList.getRegisteredListeners();
        Map<String, Set<String>> pluginListenerMap = new HashMap<>();

        for (RegisteredListener registeredListener : registeredListenerArray) {
            EventPriority listenerPriority = registeredListener.getPriority();
            if (listenerPriority != priority) {
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

    private void logDebugResults(@NotNull Class<?> eventClass, @NotNull HandlerList handlerList,
                                 @NotNull EventPriority priority) {
        Map<String, Set<String>> pluginListenerMap = getPluginListenerMap(handlerList, priority);
        Set<Entry<String, Set<String>>> entrySet = pluginListenerMap.entrySet();

        String eventClassSimpleName = eventClass.getSimpleName();
        String priorityName = priority.name();

        Replacer eventReplacer = new StringReplacer("{event}", eventClassSimpleName);
        Replacer priorityReplacer = new StringReplacer("{priority}", priorityName);

        CommandSender console = Bukkit.getConsoleSender();
        sendMessage(console, "command.debug-event.results-title", eventReplacer, priorityReplacer);

        if (pluginListenerMap.isEmpty()) {
            sendMessage(console, "command.debug-event.results-none", eventReplacer, priorityReplacer);
            return;
        }

        for (Entry<String, Set<String>> entry : entrySet) {
            String pluginName = entry.getKey();
            console.sendMessage("  " + pluginName + ":");

            Set<String> listenerClassNameSet = entry.getValue();
            for (String listenerClassName : listenerClassNameSet) {
                console.sendMessage("  - " + listenerClassName);
            }
        }
    }

    private @NotNull Set<String> getExampleEventClasses() {
        List<Class<?>> exampleClassList = Arrays.asList(PlayerInteractEvent.class, PlayerDeathEvent.class,
                PlayerTeleportEvent.class, EntitySpawnEvent.class);
        return exampleClassList.parallelStream().map(Class::getName).collect(Collectors.toSet());
    }
}
