package com.github.sirblobman.api.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public abstract class AdvancedAbstractMenu<P extends Plugin> extends BaseMenu implements Runnable {
    private final P plugin;
    private final Player player;
    private BukkitTask currentTask;

    public AdvancedAbstractMenu(@NotNull P plugin, @NotNull Player player) {
        this(null, plugin, player);
    }

    public AdvancedAbstractMenu(@Nullable IMenu parentMenu, @NotNull P plugin, @NotNull Player player) {
        super(parentMenu);
        this.plugin = plugin;
        this.player = player;
        this.currentTask = null;
    }

    @Override
    public final @NotNull P getPlugin() {
        return this.plugin;
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public final void onClose(InventoryCloseEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        if (topInventory == null) {
            return;
        }

        InventoryHolder inventoryHolder = topInventory.getHolder();
        if (!this.equals(inventoryHolder)) {
            return;
        }

        internalClose();
        onCustomClose(e);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public final void onClick(InventoryClickEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        if (topInventory == null) {
            return;
        }

        InventoryHolder inventoryHolder = topInventory.getHolder();
        if (!this.equals(inventoryHolder)) {
            return;
        }

        onValidClick(e);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public final void onDrag(InventoryDragEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        if (topInventory == null) {
            return;
        }

        InventoryHolder inventoryHolder = topInventory.getHolder();
        if (!this.equals(inventoryHolder)) {
            return;
        }

        onValidDrag(e);
    }

    public final @NotNull Player getPlayer() {
        return this.player;
    }

    public void open() {
        Player player = getPlayer();
        player.closeInventory();

        P plugin = getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, this::internalOpen, 1L);
    }

    private void internalOpen() {
        P plugin = getPlugin();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, plugin);

        Player player = getPlayer();
        Inventory inventory = getInventory();
        player.openInventory(inventory);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        this.currentTask = scheduler.runTaskTimer(plugin, this, 20L, 20L);
    }

    private void internalClose() {
        HandlerList.unregisterAll(this);
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

    protected abstract void onValidClose(@NotNull InventoryCloseEvent e);

    protected abstract void onValidClick(@NotNull InventoryClickEvent e);

    protected abstract void onValidDrag(@NotNull InventoryDragEvent e);
}
