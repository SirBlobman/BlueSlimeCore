package com.github.sirblobman.api.menu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.folia.FoliaHelper;
import com.github.sirblobman.api.folia.IFoliaPlugin;
import com.github.sirblobman.api.folia.details.EntityTaskDetails;
import com.github.sirblobman.api.folia.scheduler.BukkitTaskScheduler;
import com.github.sirblobman.api.folia.scheduler.TaskScheduler;
import com.github.sirblobman.api.menu.button.IButton;
import com.github.sirblobman.api.menu.task.AbstractMenuInternalOpenTask;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public abstract class AbstractMenu<P extends Plugin> extends BaseMenu<P> {
    private final P plugin;
    private final Player player;
    private final TaskScheduler scheduler;
    private final Map<Integer, IButton> buttonMap;

    public AbstractMenu(@NotNull P plugin, @NotNull Player player) {
        this(null, plugin, player);
    }

    public AbstractMenu(@Nullable IMenu parentMenu, @NotNull P plugin, @NotNull Player player) {
        super(parentMenu);
        if (!player.isOnline()) {
            throw new IllegalArgumentException("player must be online!");
        }

        if (plugin instanceof IFoliaPlugin) {
            FoliaHelper foliaHelper = ((IFoliaPlugin) plugin).getFoliaHelper();
            this.scheduler = foliaHelper.getScheduler();
        } else {
            this.scheduler = new BukkitTaskScheduler(plugin);
        }

        this.plugin = plugin;
        this.player = player;
        this.buttonMap = new ConcurrentHashMap<>();
    }

    @Override
    public final @NotNull P getPlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull TaskScheduler getTaskScheduler() {
        return this.scheduler;
    }

    /**
     * Override this to create a custom inventory.
     * Buttons and slots may not be set properly when using custom methods.
     *
     * @return A filled {@link Inventory} instance with this menu instance as the holder.
     */
    @Override
    public @NotNull Inventory getInventory() {
        int size = getSize();
        Component title = getTitle();
        Inventory inventory = getInventory(size, title);

        ItemStack[] contents = inventory.getContents();
        for (int slot = 0; slot < size; slot++) {
            ItemStack item = getItem(slot);
            contents[slot] = item;

            IButton button = getButton(slot);
            setButton(slot, button);
        }

        inventory.setContents(contents);
        return inventory;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public final void onClick(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!this.equals(holder)) {
            return;
        }

        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        ClickType clickType = e.getClick();
        InventoryType clickedInventoryType = clickedInventory.getType();
        if (clickedInventoryType == InventoryType.PLAYER && clickType.isShiftClick()
                && shouldCancelShiftClickFromPlayerInventory()) {
            e.setCancelled(true);
            return;
        }

        int slot = e.getSlot();
        if (shouldPreventClick(slot)) {
            e.setCancelled(true);
        }

        int rawSlot = e.getRawSlot();
        IButton button = internalGetButton(rawSlot);
        if (button != null) {
            button.onClick(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public final void onClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (!this.equals(holder)) {
            return;
        }

        onCustomClose(e);
        HandlerList.unregisterAll(this);
    }

    /**
     * @return The player that will open this menu.
     */
    public final @NotNull Player getPlayer() {
        return this.player;
    }

    @Override
    public void open() {
        P plugin = getPlugin();
        Player player = getPlayer();
        player.closeInventory();

        TaskScheduler scheduler = getTaskScheduler();
        EntityTaskDetails<Player> task = new AbstractMenuInternalOpenTask(plugin, player, this);
        scheduler.scheduleEntityTask(task);
    }

    /**
     * @return {@code true} if the event should be cancelled, otherwise {@code false}.
     */
    public boolean shouldCancelShiftClickFromPlayerInventory() {
        return true;
    }

    /**
     * Remove all previously added buttons
     */
    public void resetButtons() {
        this.buttonMap.clear();
    }

    /**
     * Set a button for this menu.
     *
     * @param slot   The slot to contain the button.
     * @param button The button instance. Use {@code null} to remove the current button.
     */
    protected final void setButton(int slot, @Nullable IButton button) {
        if (button == null) {
            this.buttonMap.remove(slot);
            return;
        }

        this.buttonMap.put(slot, button);
    }

    private @Nullable IButton internalGetButton(int slot) {
        return this.buttonMap.getOrDefault(slot, null);
    }

    /**
     * @return The size for the GUI (usually 54)
     */
    public abstract int getSize();

    /**
     * Get an ItemStack for a specific slot.
     *
     * @param slot The slot that will contain the item stack.
     * @return An {@link ItemStack} or {@code null}
     */
    public abstract @Nullable ItemStack getItem(int slot);

    /**
     * Get a button for a specific slot.
     *
     * @param slot The slot that will contain the button.
     * @return A custom button or action that will be triggered when the slot is clicked.
     * You can also return null if you do not want an action.
     */
    public abstract @Nullable IButton getButton(int slot);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @Nullable Component getTitle();

    /**
     * Override this method to change whether a click is prevented or not.
     *
     * @param slot The slot that was clicked.
     * @return {@code true} to cancel the click, {@code false} to allow the click.
     */
    public abstract boolean shouldPreventClick(int slot);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCustomClose(@NotNull InventoryCloseEvent e) {
        // Do Nothing
    }
}
