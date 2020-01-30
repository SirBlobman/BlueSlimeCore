package com.SirBlobman.api.gui;

import java.util.UUID;

import com.SirBlobman.api.utility.MessageUtil;

import org.bukkit.Bukkit;
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
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BasicGUI implements InventoryHolder, Listener {
    private final JavaPlugin plugin;
    private final UUID uuid;
    
    /**
     * This is the constructor for the BasicGUI class
     * @param plugin The {@link JavaPlugin} that will be managing the events
     * @param player The {@link Player} that will be opening the GUI
     */
    public BasicGUI(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
    }
    
    /**
     * Get the player that is attached to this GUI. This may return {@code null} if the player is no longer online
     * @return The player attached to this GUI.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    /**
     * @return The plugin attached that owns this GUI
     */
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    /**
     * Opens the inventory for the player and registers all the events for this GUI
     */
    public void open() {
        Player player = getPlayer();
        if(player == null) return;
        player.closeInventory();
        
        
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        Inventory inv = getInventory();
        if(inv != null) player.openInventory(inv);
    }
    
    /**
     * Quick method to create an inventory with a proper size and title
     * @param size The inventory size as a multiple of 9 (Max 54)
     * @param title The title to display at the top of the inventory
     * @return An inventory with this BasicGUI as the holder
     */
    public Inventory getInventory(int size, String title) {
        title = MessageUtil.color(title);
        return Bukkit.createInventory(this, size, title);
    }
    
    @Override
    public abstract Inventory getInventory();
    
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if(inventory == null) return;
        
        InventoryHolder holder = inventory.getHolder();
        if(holder == null) return;
        if(!holder.equals(this)) return;
        
        HandlerList.unregisterAll(this);
        onValidClose(e);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        if(inventory == null) return;
        
        InventoryHolder holder = inventory.getHolder();
        if(holder == null) return;
        if(!holder.equals(this)) return;
        
        e.setCancelled(true);        
        onValidClick(e);
    }
    
    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onDrag(InventoryDragEvent e) {
        Inventory inventory = e.getInventory();
        if(inventory == null) return;
        
        InventoryHolder holder = inventory.getHolder();
        if(holder == null) return;
        if(!holder.equals(this)) return;
        
        e.setCancelled(true);
        onValidDrag(e);
    }
    
    /**
     * An abstract method that allows custom GUI classes to execute stuff when an item is clicked
     * @param e The {@link InventoryClickEvent}
     */
    public abstract void onValidClick(InventoryClickEvent e);
    
    /**
     * An abstract method that allows custom GUI classes to execute stuff when the GUI is closed
     * @param e The {@link InventoryCloseEvent}
     */
    public abstract void onValidClose(InventoryCloseEvent e);
    
    /**
     * An abstract method that allows custom GUI classes to execute stuff when an item is dragged
     * @param e The {@link InventoryDragEvent}
     */
    public abstract void onValidDrag(InventoryDragEvent e);
}