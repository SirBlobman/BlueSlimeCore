package com.github.sirblobman.api.menu;

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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import com.github.sirblobman.api.menu.button.BaseMenu;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;

public abstract class AdvancedAbstractMenu<Plugin extends JavaPlugin> extends BaseMenu implements Runnable {
    private final Plugin plugin;
    private final Player player;
    private BukkitTask currentTask;

    public AdvancedAbstractMenu(Plugin plugin, Player player) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.player = Validate.notNull(player, "player must not be null!");
        this.currentTask = null;
    }

    @Override
    public final Plugin getPlugin() {
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
    public void onCustomClose(InventoryCloseEvent e) {
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

    public final Player getPlayer() {
        return this.player;
    }

    protected final Inventory getInventory(int size, String title) {
        if (title != null && !title.isEmpty()) {
            String coloredTitle = MessageUtility.color(title);
            return Bukkit.createInventory(this, size, coloredTitle);
        }

        return Bukkit.createInventory(this, size, title);
    }

    public void open() {
        Player player = getPlayer();
        player.closeInventory();

        Plugin plugin = getPlugin();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, this::internalOpen, 1L);
    }

    private void internalOpen() {
        Plugin plugin = getPlugin();
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
        if (this.currentTask != null) {
            try {
                this.currentTask.cancel();
            } catch (Exception ignored) {

            } finally {
                this.currentTask = null;
            }
        }
    }

    public abstract Inventory getInventory();

    protected abstract void onValidClose(InventoryCloseEvent e);

    protected abstract void onValidClick(InventoryClickEvent e);

    protected abstract void onValidDrag(InventoryDragEvent e);
}
