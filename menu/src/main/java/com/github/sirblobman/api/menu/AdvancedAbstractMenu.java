package com.github.sirblobman.api.menu;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.item.SkullBuilder;
import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.Validate;

import com.cryptomorin.xseries.XMaterial;

public abstract class AdvancedAbstractMenu<Plugin extends JavaPlugin> extends BukkitRunnable implements InventoryHolder, Listener {
    private final Plugin plugin;
    private final Player player;
    
    public AdvancedAbstractMenu(Plugin plugin, Player player) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.player = Validate.notNull(player, "player must not be null!");
    }
    
    /**
     * The run method in an AdvancedAbstractMenu runs once every second while the inventory is open.
     */
    @Override
    public void run() {
        // Do Nothing
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public final void onClose(InventoryCloseEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        if(topInventory == null) return;
        
        InventoryHolder inventoryHolder = topInventory.getHolder();
        if(!this.equals(inventoryHolder)) return;
        
        internalClose();
        onValidClose(e);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public final void onClick(InventoryClickEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        if(topInventory == null) return;
        
        InventoryHolder inventoryHolder = topInventory.getHolder();
        if(!this.equals(inventoryHolder)) return;
        onValidClick(e);
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public final void onDrag(InventoryDragEvent e) {
        InventoryView inventoryView = e.getView();
        Inventory topInventory = inventoryView.getTopInventory();
        if(topInventory == null) return;
        
        InventoryHolder inventoryHolder = topInventory.getHolder();
        if(!this.equals(inventoryHolder)) return;
        onValidDrag(e);
    }
    
    protected final Plugin getPlugin() {
        return this.plugin;
    }
    
    protected final Player getPlayer() {
        return this.player;
    }
    
    protected final Inventory getInventory(int size, String title) {
        if(title != null && !title.isEmpty()) {
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
        Player player = getPlayer();
        Inventory inventory = getInventory();
        player.openInventory(inventory);
        
        Plugin plugin = getPlugin();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, plugin);
        runTaskTimer(plugin, 20L, 20L);
    }
    
    private void internalClose() {
        HandlerList.unregisterAll(this);
        cancel();
    }
    
    public HeadHandler getHeadHandler() {
        return null;
    }
    
    protected final ItemStack loadItemStack(ConfigurationSection config, String path) {
        if(config.isItemStack(path)) return config.getItemStack(path);
        ConfigurationSection section = config.getConfigurationSection(path);
        return loadItemStack(section);
    }
    
    private ItemStack loadItemStack(ConfigurationSection section) {
        if(section == null) return null;
        
        String materialName = section.getString("material");
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(materialName);
        if(!xMaterial.isPresent()) {
            Logger logger = this.plugin.getLogger();
            logger.warning("Unknown material name '" + materialName + "'.");
            return null;
        }
        
        XMaterial realMaterial = xMaterial.get();
        ItemBuilder builder = new ItemBuilder(realMaterial);
        builder = checkSkull(builder, realMaterial, section);
        
        int amount = section.getInt("quantity", 1);
        builder.withAmount(amount);
        
        int damage = section.getInt("damage", 0);
        builder.withDamage(damage);
        
        Integer model = section.isSet("model") ? section.getInt("model") : null;
        builder.withModel(model);
        
        String displayName = section.getString("display-name");
        if(displayName != null) {
            String displayNameColored = MessageUtility.color(displayName);
            builder.withName(displayNameColored);
        }
        
        List<String> loreList = section.getStringList("lore");
        if(!loreList.isEmpty()) {
            List<String> loreListColored = MessageUtility.colorList(loreList);
            builder.withLore(loreListColored);
        }
        
        if(section.getBoolean("glowing")) builder.withGlowing();
        return builder.build();
    }
    
    private ItemBuilder checkSkull(ItemBuilder builder, XMaterial xMaterial, ConfigurationSection section) {
        HeadHandler headHandler = getHeadHandler();
        if(xMaterial != XMaterial.PLAYER_HEAD || headHandler == null) return builder;
        
        String texture = section.getString("texture");
        if(texture != null) return SkullBuilder.withTexture(headHandler, texture);
        
        String username = section.getString("skull-owner");
        if(username != null) return SkullBuilder.withOwner(headHandler, username);
        
        return builder;
    }
    
    public abstract Inventory getInventory();
    
    protected abstract void onValidClose(InventoryCloseEvent e);
    
    protected abstract void onValidClick(InventoryClickEvent e);
    
    protected abstract void onValidDrag(InventoryDragEvent e);
}
