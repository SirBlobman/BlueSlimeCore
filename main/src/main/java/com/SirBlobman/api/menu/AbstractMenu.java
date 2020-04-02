package com.SirBlobman.api.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.SirBlobman.api.menu.button.MenuButton;
import com.SirBlobman.api.utility.MessageUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class AbstractMenu<P extends JavaPlugin> implements InventoryHolder, Listener {
    protected final P plugin;
    private final UUID playerId;
    private final Map<Integer, MenuButton> buttonMap;
    
    /**
     * Constructor for the AbstractMenu class
     * @param plugin The {@link JavaPlugin} instance that will be handling events
     * @param player The {@link Player} that opened the menu
     */
    public AbstractMenu(P plugin, Player player) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must mot be null!");
        this.playerId = Objects.requireNonNull(player, "player must not be null!").getUniqueId();
        this.buttonMap = new HashMap<>();
    }
    
    /**
     * @return The plugin that is managing this menu's events
     */
    public final P getPlugin() {
        return this.plugin;
    }
    
    /**
     * @return The {@link UUID} of the player who opened this menu
     */
    public final UUID getPlayerId() {
        return this.playerId;
    }
    
    /**
     * @return The {@link Player} that opened this menu, or null if they're not online
     */
    public final Player getPlayer() {
        UUID uuid = getPlayerId();
        return Bukkit.getPlayer(uuid);
    }
    
    /**
     * Open this custom menu for the player and register the event handler
     * @see #getPlayer()
     * @see Player#openInventory(Inventory) 
     */
    public final void open() {
        Player player = getPlayer();
        if(player == null) return;
        player.closeInventory();
        
        Runnable task = () -> {
            Inventory inventory = getInventory();
            player.openInventory(inventory);
    
            PluginManager manager = Bukkit.getPluginManager();
            manager.registerEvents(this, this.plugin);
        };
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(this.plugin, task, 1L);
    }
    
    /**
     * Close this custom menu for this player and unregister the event handler
     * @see #getPlayer() 
     * @see Player#closeInventory() 
     */
    public final void close() {
        HandlerList.unregisterAll(this);
        
        Player player = getPlayer();
        if(player == null) return;
    
        Runnable task = () -> {
            InventoryView playerView = player.getOpenInventory();
            if(playerView == null) return;
    
            Inventory topInventory = playerView.getTopInventory();
            if(topInventory == null) return;
    
            InventoryHolder holder = topInventory.getHolder();
            if(!this.equals(holder)) return;
    
            player.closeInventory();
        };
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(this.plugin, task, 1L);
    }
    
    /**
     * @param size The number of slots in the inventory
     * @param title The title of the inventory with color codes
     * @return a custom inventory with a valid size and title
     * 
     * @throws IllegalArgumentException if the size is less than 9
     * @throws IllegalArgumentException if the size is greater than 54
     * @throws IllegalArgumentException if the size is not a multiple of 9
     */
    protected final Inventory getInventory(int size, String title) {
        if(size < 9) throw new IllegalArgumentException("size must not be less than 9");
        if(size > 54) throw new IllegalArgumentException("size must not be greater than 54");
        if((size % 9) != 0) throw new IllegalArgumentException("size must be a multiple of 9");
        
        String coloredTitle = MessageUtil.color(title);
        return Bukkit.createInventory(this, size, coloredTitle);
    }
    
    /**
     * Remove all the buttons linked to this custom menu
     */
    protected final void clearButtons() {
        this.buttonMap.clear();
    }
    
    /**
     * Add a custom button to this menu
     * @param slot The slot id that will trigger the button action when clicked
     * @param button The {@link MenuButton} that contains the action
     * @see MenuButton#onClick(InventoryClickEvent)
     */
    protected final void addButton(int slot, MenuButton button) {
        if(slot < 0 || slot > 54) throw new IndexOutOfBoundsException("slot must be between 0 and 54");
        this.buttonMap.put(slot, Objects.requireNonNull(button, "button must not be null!"));
    }
    
    /**
     * Remove a custom button from this menu
     * @param slot The slot id that has a button
     */
    protected final void removeButton(int slot) {
        if(slot < 0 || slot > 54) throw new IndexOutOfBoundsException("slot must be between 0 and 54");
        this.buttonMap.remove(slot);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public final void onClick(InventoryClickEvent e) {
        HumanEntity human = e.getWhoClicked();
        Player player = getPlayer();
        if(!human.equals(player)) return;
        
        Inventory inventory = getTargetInventory(e);
        if(inventory == null) return;
        
        InventoryHolder holder = inventory.getHolder();
        if(!this.equals(holder)) return;
        e.setCancelled(true);
        
        int slot = e.getSlot();
        MenuButton menuButton = this.buttonMap.getOrDefault(slot, null);
        if(menuButton != null) menuButton.onClick(e);
        
        onValidClick(e);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public final void onDrag(InventoryDragEvent e) {
        HumanEntity human = e.getWhoClicked();
        Player player = getPlayer();
        if(!human.equals(player)) return;
        
        Inventory inventory = e.getInventory();
        if(inventory == null) return;
        
        InventoryHolder holder = inventory.getHolder();
        if(!this.equals(holder)) return;
        e.setCancelled(true);
        
        onValidDrag(e);
    }
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public final void onClose(InventoryCloseEvent e) {
        HumanEntity human = e.getPlayer();
        Player player = getPlayer();
        if(!human.equals(player)) return;
    
        Inventory inventory = e.getInventory();
        if(inventory == null) return;
    
        InventoryHolder holder = inventory.getHolder();
        if(!this.equals(holder)) return;
        
        close();
        onValidClose(e);
    }
    
    private Inventory getTargetInventory(InventoryClickEvent e) {
        boolean shift = e.isShiftClick();
        if(shift) {
            InventoryView view = e.getView();
            return (view == null ? e.getClickedInventory() : view.getTopInventory());
        }
        
        return e.getClickedInventory();
    }
    
    /**
     * Use this method to setup the inventory items and buttons
     * @return The {@link Inventory} for this custom menu with all the items
     */
    @Override
    public abstract Inventory getInventory();
    
    /**
     * Override this to trigger actions when players click in this custom menu
     * @param e The {@link InventoryClickEvent} that triggered this action
     */
    public abstract void onValidClick(InventoryClickEvent e);
    
    /**
     * Override this to trigger actions when players drag items in this custom menu
     * @param e The {@link InventoryDragEvent} that triggered this action
     */
    public abstract void onValidDrag(InventoryDragEvent e);
    
    /**
     * Override this to trigger actions when this custom menu is closed
     * @param e The {@link InventoryCloseEvent} that triggered this action
     */
    public abstract void onValidClose(InventoryCloseEvent e);
}