package com.SirBlobman.api.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public BasicGUI(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    public void open() {
        Player player = getPlayer();
        if(player == null) return;
        player.closeInventory();
        
        
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        Inventory inv = getInventory();
        if(inv != null) player.openInventory(inv);
    }
    
    public Inventory getInventory(int size, String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);
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
    
    public abstract void onValidClick(InventoryClickEvent e);
    public abstract void onValidClose(InventoryCloseEvent e);
    public abstract void onValidDrag(InventoryDragEvent e);
}