package com.SirBlobman.api.nms;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ItemHandler_1_16_R3 extends ItemHandler {
    public ItemHandler_1_16_R3(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLocalizedName(ItemStack item) {
        if(item == null) return "Air";

        ItemMeta meta = item.getItemMeta();
        if(meta != null && meta.hasDisplayName()) return meta.getDisplayName();

        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName().getText();
    }

    @Override
    public String toNBT(ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        net.minecraft.server.v1_16_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        nmsItem.save(nbtData);
        return nbtData.toString();
    }

    @Override
    public ItemStack fromNBT(String string) {
        try {
            NBTTagCompound nbtData = MojangsonParser.parse(string);
            net.minecraft.server.v1_16_R3.ItemStack nmsItem = net.minecraft.server.v1_16_R3.ItemStack.a(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(CommandSyntaxException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        if(item == null || key == null || key.isEmpty() || value == null) return item;

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;

        JavaPlugin plugin = getPlugin();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, value);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getCustomNBT(ItemStack item, String key, String defaultValue) {
        if(item == null || key == null || key.isEmpty()) return defaultValue;

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return defaultValue;

        JavaPlugin plugin = getPlugin();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        return persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.STRING, defaultValue);
    }

    @Override
    public ItemStack removeCustomNBT(ItemStack item, String key) {
        if(item == null || key == null || key.isEmpty()) return item;

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;

        JavaPlugin plugin = getPlugin();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        persistentDataContainer.remove(namespacedKey);

        item.setItemMeta(meta);
        return item;
    }
}