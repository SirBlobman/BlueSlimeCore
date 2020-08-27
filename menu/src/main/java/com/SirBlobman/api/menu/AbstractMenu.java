package com.SirBlobman.api.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.SirBlobman.api.item.ItemBuilder;
import com.SirBlobman.api.menu.button.AbstractButton;
import com.SirBlobman.api.utility.MessageUtility;
import com.SirBlobman.api.utility.Validate;

import com.cryptomorin.xseries.XMaterial;

public abstract class AbstractMenu implements InventoryHolder, Listener {
    private final JavaPlugin plugin;
    private final Player player;
    private final Map<Integer, AbstractButton> buttonMap;
    public AbstractMenu(JavaPlugin plugin, Player player) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.player = Validate.notNull(player, "player must not be null!");
        if(!this.player.isOnline()) throw new IllegalArgumentException("player must be online!");
        this.buttonMap = new HashMap<>();
    }

    @Override
    public Inventory getInventory() {
        int size = getSize();
        String title = getTitle();
        Inventory inventory = getInventory(size, title);

        ItemStack[] contents = inventory.getContents();
        for(int slot = 0; slot < size; slot++) {
            ItemStack item = getItem(slot);
            contents[slot] = item;

            AbstractButton button = getButton(slot);
            setButton(slot, button);
        }

        inventory.setContents(contents);
        return inventory;
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
    public final void onClick(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if(!this.equals(holder)) return;

        Inventory clickedInventory = e.getClickedInventory();
        InventoryType clickedInventoryType = clickedInventory.getType();
        ClickType clickType = e.getClick();
        if(clickedInventoryType == InventoryType.PLAYER && clickType.isShiftClick()) {
            e.setCancelled(true);
            return;
        }

        int slot = e.getSlot();
        if(shouldPreventClick(slot)) e.setCancelled(true);

        AbstractButton button = internalGetButton(slot);
        if(button != null) button.onClick(e);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if(!this.equals(holder)) return;

        HandlerList.unregisterAll(this);
    }

    public final Inventory getInventory(int size, String title) {
        if(size < 9) throw new IllegalArgumentException("size must be at least 9");
        if(size > 54) throw new IllegalArgumentException("size cannot be more than 54");
        if(size % 9 != 0) throw new IllegalArgumentException("size must be divisible by 9");

        String realTitle = MessageUtility.color(title);
        return Bukkit.createInventory(this, size, realTitle);
    }

    public final JavaPlugin getPlugin() {
        return this.plugin;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final void open() {
        JavaPlugin plugin = getPlugin();
        Player player = getPlayer();
        player.closeInventory();

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, this::internalOpen);
    }

    protected final void setButton(int slot, AbstractButton button) {
        if(button == null) {
            this.buttonMap.remove(slot);
            return;
        }

        this.buttonMap.put(slot, button);
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

        int amount = section.getInt("quantity");
        builder.withAmount(amount);

        int damage = section.getInt("damage");
        builder.withDamage(damage);

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

    private void internalOpen() {
        this.buttonMap.clear();
        JavaPlugin plugin = getPlugin();
        Inventory inventory = getInventory();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, plugin);

        Player player = getPlayer();
        player.openInventory(inventory);
    }

    private AbstractButton internalGetButton(int slot) {
        return this.buttonMap.getOrDefault(slot, null);
    }

    public abstract int getSize();
    public abstract String getTitle();
    public abstract ItemStack getItem(int slot);
    public abstract AbstractButton getButton(int slot);
    public abstract boolean shouldPreventClick(int slot);
}
