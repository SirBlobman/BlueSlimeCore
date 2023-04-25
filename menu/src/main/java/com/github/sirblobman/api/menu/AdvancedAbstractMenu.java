package com.github.sirblobman.api.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.IFoliaPlugin;
import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.folia.task.WrappedTask;
import com.github.sirblobman.api.menu.task.AdvancedMenuInternalOpenTask;
import com.github.sirblobman.api.menu.task.AdvancedMenuRefreshLoopTask;

public abstract class AdvancedAbstractMenu<P extends Plugin> extends BaseMenu<P> implements Runnable {
    private final IFoliaPlugin<P> plugin;
    private final Player player;
    private WrappedTask currentTask;

    public AdvancedAbstractMenu(@NotNull IFoliaPlugin<P> plugin, @NotNull Player player) {
        this(null, plugin, player);
    }

    public AdvancedAbstractMenu(@Nullable IMenu<P> parentMenu, @NotNull IFoliaPlugin<P> plugin,
                                @NotNull Player player) {
        super(parentMenu);
        this.plugin = plugin;
        this.player = player;
        this.currentTask = null;
    }

    @Override
    public final @NotNull IFoliaPlugin<P> getFoliaPlugin() {
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
        P plugin = getPlugin();
        Player player = getPlayer();
        player.closeInventory();

        IFoliaPlugin<P> foliaPlugin = getFoliaPlugin();
        FoliaHelper<P> foliaHelper = foliaPlugin.getFoliaHelper();
        TaskScheduler<P> scheduler = foliaHelper.getScheduler();

        EntityTaskDetails<P, Player> task = new AdvancedMenuInternalOpenTask<>(plugin, player, this);
        scheduler.scheduleEntityTask(task);

        EntityTaskDetails<P, Player> timer = new AdvancedMenuRefreshLoopTask<>(plugin, player, this);
        this.currentTask = scheduler.scheduleEntityTask(timer);
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
