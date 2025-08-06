package com.github.sirblobman.api.menu;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.FoliaPlugin;
import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.folia.scheduler.BukkitTaskScheduler;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.folia.task.WrappedTask;
import com.github.sirblobman.api.menu.task.AdvancedMenuInternalOpenTask;
import com.github.sirblobman.api.menu.task.AdvancedMenuRefreshLoopTask;

public abstract class AdvancedAbstractMenu<P extends Plugin> extends BaseMenu<P> implements Runnable {
    private final P plugin;
    private final TaskScheduler scheduler;
    private final Player player;
    private WrappedTask currentTask;

    private final List<Listener> listenerList;

    public AdvancedAbstractMenu(@NotNull P plugin, @NotNull Player player) {
        this(null, plugin, player);
    }

    public AdvancedAbstractMenu(@Nullable IMenu parentMenu, @NotNull P plugin, @NotNull Player player) {
        super(parentMenu);
        this.plugin = plugin;

        if (plugin instanceof FoliaPlugin) {
            FoliaHelper foliaHelper = ((FoliaPlugin) plugin).getFoliaHelper();
            this.scheduler = foliaHelper.getScheduler();
        } else {
            this.scheduler = new BukkitTaskScheduler(plugin);
        }

        this.player = player;
        this.currentTask = null;
        this.listenerList = new ArrayList<>();
    }

    @Override
    public @NotNull P getPlugin() {
        return this.plugin;
    }

    @Override
    public void registerListeners() {
        super.registerListeners();

        PluginManager pluginManager = Bukkit.getPluginManager();
        Listener listener = getInventoryListener();
        this.listenerList.add(listener);
        pluginManager.registerEvents(listener, getPlugin());
    }

    private @NotNull Listener getInventoryListener() {
        try {
            String packageName = AdvancedAbstractMenu.class.getPackageName();
            Class<?> class_InventoryView = Class.forName("org.bukkit.inventory.InventoryView");
            if (class_InventoryView.isInterface()) {
                // New Interface InventoryView
                Class<?> newListener = Class.forName(packageName + ".listener.NewInventoryListener");
                Constructor<?> constructor = newListener.getConstructor(AdvancedAbstractMenu.class);
                return (Listener) constructor.newInstance(this);
            } else {
                // Legacy Abstract Class InventoryView
                Class<?> newListener = Class.forName(packageName + ".listener.InventoryListener");
                Constructor<?> constructor = newListener.getConstructor(AdvancedAbstractMenu.class);
                return (Listener) constructor.newInstance(this);
            }
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("InventoryView class does not exist in the expected path.", ex);
        }
    }

    @Override
    public @NotNull TaskScheduler getTaskScheduler() {
        return this.scheduler;
    }

    /**
     * The run method in an AdvancedAbstractMenu runs once every second while the inventory is open.
     */
    @Override
    public void run() {
        // Do Nothing
    }

    @Override
    public void onCustomClose(@NotNull InventoryCloseEvent e) {
        onValidClose(e);
    }

    public final @NotNull Player getPlayer() {
        return this.player;
    }

    public void open() {
        P plugin = getPlugin();
        Player player = getPlayer();
        player.closeInventory();

        TaskScheduler scheduler = getTaskScheduler();
        EntityTaskDetails<Player> task = new AdvancedMenuInternalOpenTask(plugin, player, this);
        scheduler.scheduleEntityTask(task);

        EntityTaskDetails<Player> timer = new AdvancedMenuRefreshLoopTask(plugin, player, this);
        this.currentTask = scheduler.scheduleEntityTask(timer);
    }

    /**
     * Do not call this method. It is meant for internal use only.
     * This will unregister the listeners and stop all running tasks.
     */
    public void internalClose() {
        for (Listener listener : this.listenerList) {
            HandlerList.unregisterAll(listener);
        }

        if (this.currentTask == null) {
            return;
        }

        try {
            this.currentTask.cancel();
        } catch (Exception ignored) {
            // Do Nothing
        } finally {
            this.currentTask = null;
        }
    }

    public abstract @NotNull Inventory getInventory();

    public abstract void onValidClose(@NotNull InventoryCloseEvent e);

    public abstract void onValidClick(@NotNull InventoryClickEvent e);

    public abstract void onValidDrag(@NotNull InventoryDragEvent e);
}
