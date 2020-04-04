package com.SirBlobman.api.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.SirBlobman.api.item.ItemUtil;
import com.SirBlobman.api.menu.button.MenuButton;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPagedMenu<P extends JavaPlugin> extends AbstractMenu<P> {
    private final Map<Integer, Map<Integer, MenuButton>> pagedButtonMap;
    private int currentPage;
    public AbstractPagedMenu(P plugin, Player player) {
        super(plugin, player);
        this.pagedButtonMap = new HashMap<>();
        this.currentPage = 1;
    }
    
    public final int getCurrentPage() {
        return this.currentPage;
    }
    
    protected final void clearPagedButtons() {
        this.pagedButtonMap.clear();
    }
    
    protected final void clearPagedButtons(int page) {
        if(page < 1) throw new IllegalArgumentException("page must not be less than 1");
        
        Map<Integer, MenuButton> buttonMap = this.pagedButtonMap.getOrDefault(page, new HashMap<>());
        buttonMap.clear();
        this.pagedButtonMap.put(page, buttonMap);
    }
    
    protected final void addPagedButton(int page, int slot, MenuButton button) {
        if(page < 1) throw new IllegalArgumentException("page must not be less than 1");
        if(slot < 0 || slot > 54) throw new IndexOutOfBoundsException("slot must be between 0 and 54");
        
        Map<Integer, MenuButton> buttonMap = this.pagedButtonMap.getOrDefault(page, new HashMap<>());
        buttonMap.put(slot, button);
        this.pagedButtonMap.put(page, buttonMap);
    }
    
    protected final void removePagedButton(int page, int slot) {
        if(page < 1) throw new IllegalArgumentException("page must not be less than 1");
        if(slot < 0 || slot > 54) throw new IndexOutOfBoundsException("slot must be between 0 and 54");
        
        Map<Integer, MenuButton> buttonMap = this.pagedButtonMap.getOrDefault(page, new HashMap<>());
        buttonMap.remove(slot);
        this.pagedButtonMap.put(page, buttonMap);
    }
    
    @Override
    public final Inventory getInventory() {
        String title = getInventoryTitle();
        Inventory inventory = getInventory(54, title);
        ItemStack[] contents = inventory.getContents();
        
        ItemStack fillerItem = getFillerItem();
        Arrays.fill(contents, fillerItem);
        
        int currentPage = getCurrentPage();
        int maxPages = getMaxPages();
        
        if(currentPage > 1) contents[45] = getPreviousPageItem();
        if(currentPage < maxPages) contents[54] = getNextPageItem();
        inventory.setContents(contents);
        
        updatePage(inventory);
        return inventory;
    }
    
    @Override
    public final void onValidClick(InventoryClickEvent e) {
        int page = getCurrentPage();
        int slot = e.getSlot();
    
        Map<Integer, MenuButton> buttonMap = this.pagedButtonMap.getOrDefault(page, new HashMap<>());
        MenuButton menuButton = buttonMap.getOrDefault(slot, null);
        if(menuButton != null) menuButton.onClick(e);
    }
    
    private void openNextPage(Inventory inventory) {
        int maxPages = getMaxPages();
        this.currentPage = Math.min(maxPages, this.currentPage + 1);
        updatePage(inventory);
    }
    
    private void openPreviousPage(Inventory inventory) {
        this.currentPage = Math.max(0, this.currentPage - 1);
        updatePage(inventory);
    }
    
    private void updatePage(Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        List<ItemStack> itemList = getCurrentItems();
        int itemListSize = itemList.size();
    
        for(int slot = 0; slot < 45 && slot < itemListSize; slot++) {
            ItemStack item = itemList.get(slot);
            if(ItemUtil.isAir(item)) continue;
            contents[slot] = item.clone();
        }
        inventory.setContents(contents);
    }
    
    protected abstract int getMaxPages();
    protected abstract String getInventoryTitle();
    protected abstract List<ItemStack> getCurrentItems();
    protected abstract ItemStack getFillerItem();
    protected abstract ItemStack getNextPageItem();
    protected abstract ItemStack getPreviousPageItem();
}