package com.github.sirblobman.api.nms;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.MojangsonParser;
import net.minecraft.server.v1_7_R4.NBTBase;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;

public class ItemHandler_1_7_R4 extends ItemHandler {
    public ItemHandler_1_7_R4(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLocalizedName(ItemStack item) {
        if(item == null) return "Air";

        ItemMeta meta = item.getItemMeta();
        if(meta.hasDisplayName()) return meta.getDisplayName();

        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName();
    }

    @Override
    public String getKeyString(ItemStack item) {
        if(item == null) return "minecraft:air";
        net.minecraft.server.v1_7_R4.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        Item nmsItem = nmsItemStack.getItem();
        return Item.REGISTRY.c(nmsItem);
    }

    @Override
    public String toNBT(ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        nmsItem.save(nbtData);
        return nbtData.toString();
    }

    @Override
    public ItemStack fromNBT(String string) {
        try {
            NBTBase parse = MojangsonParser.parse(string);
            if(!(parse instanceof NBTTagCompound)) {
                JavaPlugin plugin = getPlugin();
                Logger logger = plugin.getLogger();
                logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", new IllegalStateException("'" + string + "' is not an NBTTagCompound."));
                return new ItemStack(Material.AIR);
            }

            NBTTagCompound nbtData = (NBTTagCompound) parse;
            net.minecraft.server.v1_7_R4.ItemStack nmsItem = net.minecraft.server.v1_7_R4.ItemStack.createStack(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(Exception ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        if(item == null || key == null || key.isEmpty() || value == null) return item;

        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.setString(key, value);
        nbtData.set(pluginName, customData);

        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public String getCustomNBT(ItemStack item, String key, String defaultValue) {
        if(item == null || key == null || key.isEmpty()) return defaultValue;

        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        String string = customData.getString(key);
        return (string == null ? defaultValue : string);
    }

    @Override
    public ItemStack removeCustomNBT(ItemStack item, String key) {
        if(item == null || key == null || key.isEmpty()) return item;

        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.remove(key);

        if (customData.isEmpty()) nbtData.remove(pluginName);
        else nbtData.set(pluginName, customData);

        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}