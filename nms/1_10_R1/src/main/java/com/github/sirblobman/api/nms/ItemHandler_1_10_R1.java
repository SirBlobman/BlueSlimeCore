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

import net.minecraft.server.v1_10_R1.Item;
import net.minecraft.server.v1_10_R1.ItemStack;
import net.minecraft.server.v1_10_R1.MinecraftKey;
import net.minecraft.server.v1_10_R1.MojangsonParseException;
import net.minecraft.server.v1_10_R1.MojangsonParser;
import net.minecraft.server.v1_10_R1.NBTBase;
import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContainer_1_10_R1;
import com.github.sirblobman.api.nbt.CustomNbtTypeRegistry_1_10_R1;
import com.github.sirblobman.api.utility.ItemUtility;

import org.jetbrains.annotations.Contract;

public class ItemHandler_1_10_R1 extends ItemHandler {
    private final CustomNbtTypeRegistry_1_10_R1 nbtRegistry;

    public ItemHandler_1_10_R1(JavaPlugin plugin) {
        super(plugin);
        this.nbtRegistry = new CustomNbtTypeRegistry_1_10_R1();
    }

    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack item) {
        if (item == null) {
            return "Air";
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null && itemMeta.hasDisplayName()) {
            return itemMeta.getDisplayName();
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName();
    }

    @Override
    public String getKeyString(org.bukkit.inventory.ItemStack item) {
        if (item == null) {
            return "minecraft:air";
        }

        ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
        Item nmsItem = nmsItemStack.getItem();

        MinecraftKey minecraftKey = Item.REGISTRY.b(nmsItem);
        if (minecraftKey == null) {
            return "minecraft:air";
        }

        return minecraftKey.toString();
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
            NBTTagCompound nbtData = MojangsonParser.parse(string);
            ItemStack nmsItem = ItemStack.createStack(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (MojangsonParseException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }
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
    public org.bukkit.inventory.ItemStack setDisplayName(org.bukkit.inventory.ItemStack item, Component displayName) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return item;
        }

        String legacy = ComponentHelper.toLegacy(displayName);
        itemMeta.setDisplayName(legacy);

        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public org.bukkit.inventory.ItemStack setLore(org.bukkit.inventory.ItemStack item, List<Component> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
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

    @Override
    public CustomNbtContainer createNewCustomNbtContainer() {
        return new CustomNbtContainer_1_10_R1(this.nbtRegistry);
    }

    @Override
    public org.bukkit.inventory.ItemStack setCustomNbt(org.bukkit.inventory.ItemStack item,
                                                       CustomNbtContainer customNbtContainer) {
        if (item == null) {
            return null;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = createNBT(nmsItem);

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTBase wrap = this.nbtRegistry.wrap(CustomNbtContainer.class, customNbtContainer);
        nbtData.set(pluginName, wrap);

        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public CustomNbtContainer getCustomNbt(org.bukkit.inventory.ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = createNBT(nmsItem);

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
        NBTTagCompound compound = nbtData.getCompound(pluginName);
        return this.nbtRegistry.extract(CustomNbtContainer.class, compound);
    }

    @Contract("null -> null")
    private NBTTagCompound createNBT(ItemStack nmsItem) {
        if (nmsItem == null) {
            return null;
        }

        if (!nmsItem.hasTag()) {
            return new NBTTagCompound();
        }

        NBTTagCompound tag = nmsItem.getTag();
        return (tag == null ? new NBTTagCompound() : tag);
    }
}
