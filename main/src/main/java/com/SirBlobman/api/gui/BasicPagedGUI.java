package com.SirBlobman.api.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.utility.ItemUtil;

public abstract class BasicPagedGUI extends BasicGUI {
    public BasicPagedGUI(JavaPlugin plugin, Player player) {super(plugin, player);}

    protected static final ItemStack BACK_ITEM = ItemUtil.newItem(Material.PAPER, 1, 0, "&7Previous Page");
    protected static final ItemStack NEXT_ITEM = ItemUtil.newItem(Material.PAPER, 1, 0, "&7Next Page");
    protected int page = 1;
    
    public void openNextPage() {
        Player player = getPlayer();
        if(player != null) {
            player.closeInventory();
            page = Math.min(page + 1, getMaxPages());
            HandlerList.unregisterAll(this);
            open();
        }
    }
    
    public void openPreviousPage() {
        Player player = getPlayer();
        if(player != null) {
            player.closeInventory();
            page = Math.max(page - 1, 1);
            HandlerList.unregisterAll(this);
            open();
        }
    }
    
    public Inventory getInventory(int size, String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);
        return Bukkit.createInventory(this, size, title);
    }
    
    @Override
    public Inventory getInventory() {
        Inventory inventory = getInventory(54, getInventoryTitle());
        final int invSize = inventory.getSize();
        
        List<ItemStack> itemList = getInventoryItems();
        final int itemListSize = itemList.size();
        
        int startIndex = ((invSize - 18) * (page - 1));
        int endIndex = (startIndex + (invSize - 18));
        int slotIndex = 0;
        
        for(int i = startIndex; (i < itemListSize && i < endIndex); i++) {
            ItemStack item = itemList.get(i);
            if(ItemUtil.isAir(item)) item = ItemUtil.getAir();
            
            inventory.setItem(slotIndex, item);
            slotIndex++;
        }
        
        ItemStack filler = getFillerItem();
        for(int i = 36; i < 45; i++) inventory.setItem(i, filler);
        
        inventory.setItem(45, startIndex > 0 ? BACK_ITEM : ItemUtil.getAir());
        inventory.setItem(53, endIndex < itemListSize ? NEXT_ITEM : ItemUtil.getAir());
        
        return inventory;
    }
    
    @Override
    public void onValidClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if(BACK_ITEM.equals(item)) {
            openPreviousPage();
            return;
        }
        
        if(NEXT_ITEM.equals(item)) {
            openNextPage();
            return;
        }
        
        onCustomClick(e);
    }
    
    public abstract int getMaxPages();
    public abstract List<ItemStack> getInventoryItems();
    public abstract String getInventoryTitle();
    public abstract void onValidClose(InventoryCloseEvent e);
    public abstract void onValidDrag(InventoryDragEvent e);
    public abstract void onCustomClick(InventoryClickEvent e);
    public abstract ItemStack getFillerItem();
}