package com.github.sirblobman.api.nms;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;

public class ItemHandler_1_10_R1 extends ItemHandler {
    public ItemHandler_1_10_R1(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLocalizedName(ItemStack item) {
        if(item == null) return "Air";

        ItemMeta meta = item.getItemMeta();
        if(meta.hasDisplayName()) return meta.getDisplayName();

        net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName();
    }

    @Override
    public String getKeyString(ItemStack item) {
        if(item == null) return "minecraft:air";
        net.minecraft.server.v1_10_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        Item nmsItem = nmsItemStack.getItem();

        MinecraftKey minecraftKey = Item.REGISTRY.b(nmsItem);
        if(minecraftKey == null) return "minecraft:air";
        return minecraftKey.toString();
    }

    @Override
    public String toNBT(ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        nmsItem.save(nbtData);
        return nbtData.toString();
    }

    @Override
    public ItemStack fromNBT(String string) {
        try {
            NBTTagCompound nbtData = MojangsonParser.parse(string);
            net.minecraft.server.v1_10_R1.ItemStack nmsItem = net.minecraft.server.v1_10_R1.ItemStack.createStack(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(MojangsonParseException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        if(item == null || key == null || key.isEmpty() || value == null) return item;

        net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
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

        net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
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

        net.minecraft.server.v1_10_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
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