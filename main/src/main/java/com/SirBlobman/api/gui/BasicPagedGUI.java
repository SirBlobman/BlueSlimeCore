package com.SirBlobman.api.gui;

import java.util.List;

import com.SirBlobman.api.item.ItemUtil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BasicPagedGUI extends BasicGUI {
    public BasicPagedGUI(JavaPlugin plugin, Player player) {super(plugin, player);}

    protected static final ItemStack BACK_ITEM = ItemUtil.newItem(Material.PAPER, 1, 0, "&7Previous Page");
    protected static final ItemStack NEXT_ITEM = ItemUtil.newItem(Material.PAPER, 1, 0, "&7Next Page");
    protected int page = 1;
    
    /**
     * Open the next page or reopen the current page if the next page does not exist.
     */
    public void openNextPage() {
        Player player = getPlayer();
        if(player == null) return;
        
        player.closeInventory();
        this.page = Math.min(this.page + 1, getMaxPages());
        
        HandlerList.unregisterAll(this);
        open();
    }
    
    /**
     * Open the previous page or reopen the current page if the next page does not exist.
     */
    public void openPreviousPage() {
        Player player = getPlayer();
        if(player == null) return;
        
        player.closeInventory();
        this.page = Math.max(this.page - 1, 1);
        
        HandlerList.unregisterAll(this);
        open();
    }
    
    @Override
    public final Inventory getInventory() {
        String title = getInventoryTitle();
        Inventory inventory = getInventory(54, title);
        final int invSize = inventory.getSize();
        
        List<ItemStack> itemList = getInventoryItems();
        final int itemListSize = itemList.size();
        
        int startIndex = ((invSize - 9) * (page - 1));
        int endIndex = (startIndex + (invSize - 9));
        int slotIndex = 0;
        
        for(int index = startIndex; (index < itemListSize && index < endIndex); index++) {
            ItemStack item = itemList.get(index);
            if(ItemUtil.isAir(item)) item = ItemUtil.getAir();
            
            inventory.setItem(slotIndex, item);
            slotIndex++;
        }
        
        ItemStack filler = getFillerItem();
        for(int slot = 45; slot < 54; slot++) inventory.setItem(slot, filler);
        
        inventory.setItem(45, startIndex > 0 ? BACK_ITEM : filler);
        inventory.setItem(53, endIndex < itemListSize ? NEXT_ITEM : filler);
        
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
    
    /**
     * Get the highest page number possible for this GUI
     * By default it checks the amount of available items
     * @return The maximum number of pages for this GUI
     */
    public int getMaxPages() {
        List<ItemStack> itemList = getInventoryItems();
        int itemListSize = itemList.size();

        int extra = ((itemListSize % 36) == 0 ? 0 : 1);
        return ((itemListSize / 36) + extra);
    }
    
    /**
     * @return All of the items that will be placed into this GUI
     */
    public abstract List<ItemStack> getInventoryItems();
    
    /**
     * @return The title for this GUI
     */
    public abstract String getInventoryTitle();
    
    /**
     * An abstract method that allows custom GUI classes to execute stuff when an item is clicked
     * @param e The {@link InventoryClickEvent}
     */
    public abstract void onCustomClick(InventoryClickEvent e);
    
    /**
     * @return The item that will be used to fill empty slots in this GUI
     */
    public abstract ItemStack getFillerItem();
}