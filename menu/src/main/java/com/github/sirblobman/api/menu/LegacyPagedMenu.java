package com.github.sirblobman.api.menu;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.menu.button.AbstractButton;
import com.github.sirblobman.api.menu.button.NextPageButton;
import com.github.sirblobman.api.menu.button.PreviousPageButton;
import com.github.sirblobman.api.utility.ItemUtility;

/**
 * @deprecated Use {@link AbstractPagedMenu}
 */
@Deprecated
public abstract class LegacyPagedMenu extends AbstractPagedMenu {
    public LegacyPagedMenu(JavaPlugin plugin, Player player) {
        super(plugin, player);
    }
    
    @Override
    public Inventory getInventory() {
        String title = getTitle();
        Inventory inventory = getInventory(54, title);
        int invSize = inventory.getSize();
        
        List<ItemStack> itemList = getInventoryItems();
        int itemListSize = itemList.size();
        
        int page = getCurrentPage();
        int startIndex = ((invSize - 18) * (page - 1));
        int endIndex = (startIndex + (invSize - 18));
        int slotIndex = 0;
        
        for(int i = startIndex; (i < itemListSize && i < endIndex); i++) {
            ItemStack item = itemList.get(i);
            if(ItemUtility.isAir(item)) {
                item = ItemUtility.getAir();
            }
            
            inventory.setItem(slotIndex, item);
            slotIndex++;
        }
        
        ItemStack filler = getFillerItem();
        for(int slot = 36; slot < 45; slot++) {
            inventory.setItem(slot, filler);
            
            AbstractButton button = new AbstractButton() {
                @Override
                public void onClick(InventoryClickEvent e) {
                    onCustomClick(e);
                }
            };
            setButton(slot, button);
        }
        
        if(startIndex > 0) {
            ItemStack previousPageItem = getPreviousPageItem();
            inventory.setItem(45, previousPageItem);
            setButton(45, new PreviousPageButton(this));
        } else {
            inventory.setItem(45, ItemUtility.getAir());
        }
        
        if(endIndex < itemListSize) {
            ItemStack nextPageItem = getNextPageItem();
            inventory.setItem(53, nextPageItem);
            setButton(53, new NextPageButton(this));
        } else {
            inventory.setItem(53, ItemUtility.getAir());
        }
        
        return inventory;
    }
    
    @Override
    public int getMaxPages() {
        List<ItemStack> itemList = getInventoryItems();
        int itemListSize = itemList.size();
    
        int extra = ((itemListSize % 36) == 0 ? 0 : 1);
        return ((itemListSize / 36) + extra);
    }
    
    @Override
    public int getSize() {
        return 54;
    }
    
    @Override
    public ItemStack getItem(int slot) {
        return null;
    }
    
    @Override
    public AbstractButton getButton(int slot) {
        return null;
    }
    
    @Override
    public boolean shouldPreventClick(int slot) {
        return false;
    }
    
    public abstract String getTitleFormat();
    public abstract List<ItemStack> getInventoryItems();
    public abstract void onCustomClick(InventoryClickEvent e);
    
    public abstract ItemStack getFillerItem();
    public abstract ItemStack getNextPageItem();
    public abstract ItemStack getPreviousPageItem();
}
