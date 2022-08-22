package com.github.sirblobman.api.nms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.MojangsonParser;
import net.minecraft.server.v1_7_R4.NBTBase;
import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;

import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.utility.ItemUtility;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;

public final class ItemHandler_1_7_R4 extends ItemHandler {
    // private final CustomNbtTypeRegistry_1_7_R4 nbtRegistry;

    public ItemHandler_1_7_R4(JavaPlugin plugin) {
        super(plugin);
        // this.nbtRegistry = new CustomNbtTypeRegistry_1_7_R4();
    }

    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack item) {
        if (item == null) {
            return "Air";
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }

        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName();
    }

    @Override
    public String getKeyString(org.bukkit.inventory.ItemStack item) {
        if (item == null) {
            return "minecraft:air";
        }

        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        Item nmsItem = nmsItemStack.getItem();
        return Item.REGISTRY.c(nmsItem);
    }

    @Override
    public String toNBT(org.bukkit.inventory.ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        nmsItem.save(nbtData);
        return nbtData.toString();
    }

    @Override
    public org.bukkit.inventory.ItemStack fromNBT(String string) {
        try {
            NBTBase parse = MojangsonParser.parse(string);
            if (!(parse instanceof NBTTagCompound)) {
                JavaPlugin plugin = getPlugin();
                Logger logger = plugin.getLogger();

                String errorMessage = ("'" + string + "' is not an NBTTagCompound.");
                IllegalStateException error = new IllegalStateException(errorMessage);
                logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", error);
                return new org.bukkit.inventory.ItemStack(Material.AIR);
            }

            NBTTagCompound nbtData = (NBTTagCompound) parse;
            ItemStack nmsItem = ItemStack.createStack(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (Exception ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }
    }

    @Override
    public org.bukkit.inventory.ItemStack setCustomNBT(org.bukkit.inventory.ItemStack item, String key, String value) {
        if (item == null || key == null || key.isEmpty() || value == null) {
            return item;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if (nbtData == null) {
            nbtData = new NBTTagCompound();
        }

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.setString(key, value);
        nbtData.set(pluginName, customData);

        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public String getCustomNBT(org.bukkit.inventory.ItemStack item, String key, String defaultValue) {
        if (item == null || key == null || key.isEmpty()) {
            return defaultValue;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if (nbtData == null) {
            nbtData = new NBTTagCompound();
        }

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        String string = customData.getString(key);
        return (string == null ? defaultValue : string);
    }

    @Override
    public org.bukkit.inventory.ItemStack removeCustomNBT(org.bukkit.inventory.ItemStack item, String key) {
        if (item == null || key == null || key.isEmpty()) {
            return item;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if (nbtData == null) nbtData = new NBTTagCompound();

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.remove(key);

        if (customData.isEmpty()) nbtData.remove(pluginName);
        else nbtData.set(pluginName, customData);

        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    // TODO
//    @Override
//    public CustomNbtContainer createNewCustomNbtContainer() {
//        return new CustomNbtContainer_1_7_R4(this.nbtRegistry);
//    }
//
//    @Override
//    public org.bukkit.inventory.ItemStack setCustomNbt(org.bukkit.inventory.ItemStack item,
//                                                       CustomNbtContainer customNbtContainer) {
//        if (item == null) {
//            return null;
//        }
//
//        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
//        NBTTagCompound nbtData = createNBT(nmsItem);
//
//        JavaPlugin plugin = getPlugin();
//        String pluginName = plugin.getName();
//
//        NBTBase wrap = this.nbtRegistry.wrap(CustomNbtContainer.class, customNbtContainer);
//        nbtData.set(pluginName, wrap);
//
//        nmsItem.setTag(nbtData);
//        return CraftItemStack.asBukkitCopy(nmsItem);
//    }

    @Contract("null -> null")
    private NBTTagCompound createNBT(ItemStack nmsItem) {
        if(nmsItem == null) {
            return null;
        }

        if(!nmsItem.hasTag()) {
            return new NBTTagCompound();
        }

        NBTTagCompound tag = nmsItem.getTag();
        return (tag == null ? new NBTTagCompound() : tag);
    }

    @Override
    public org.bukkit.inventory.ItemStack fromBase64String(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        NBTTagCompound nbtTagCompound;
        byte[] decode = Base64.getDecoder().decode(string);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);

        try {
            nbtTagCompound = NBTCompressedStreamTools.a(byteArrayInputStream);
        } catch (Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to decode an item from a string because an error occurred:", ex);
            return null;
        }

        ItemStack nmsItem = ItemStack.createStack(nbtTagCompound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public String toBase64String(org.bukkit.inventory.ItemStack item) {
        if (ItemUtility.isAir(item)) {
            return null;
        }

        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CraftItemStack.asNMSCopy(item).save(nbtTagCompound);

        try {
            NBTCompressedStreamTools.a(nbtTagCompound, byteArrayOutputStream);
        } catch (Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to encode an item to a string because an error occurred:", ex);
            return null;
        }

        byte[] encode = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(encode);
    }

    @Override
    public org.bukkit.inventory.ItemStack setDisplayName(org.bukkit.inventory.ItemStack item,
                                                         net.kyori.adventure.text.Component displayName) {
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return item;
        }

        String legacy = ComponentHelper.toLegacy(displayName);
        itemMeta.setDisplayName(legacy);

        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public org.bukkit.inventory.ItemStack setLore(org.bukkit.inventory.ItemStack item,
                                                  List<Component> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return item;
        }

        List<String> legacyLore = new ArrayList<>();
        for (Component line : lore) {
            String legacy = ComponentHelper.toLegacy(line);
            legacyLore.add(legacy);
        }

        itemMeta.setLore(legacyLore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
