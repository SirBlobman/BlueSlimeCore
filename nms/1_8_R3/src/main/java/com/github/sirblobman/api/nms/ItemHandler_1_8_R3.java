package com.github.sirblobman.api.nms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContainer_1_8_R3;
import com.github.sirblobman.api.nbt.CustomNbtTypeRegistry_1_8_R3;
import com.github.sirblobman.api.shaded.adventure.text.Component;

public final class ItemHandler_1_8_R3 extends ItemHandler {
    private final CustomNbtTypeRegistry_1_8_R3 registry;

    public ItemHandler_1_8_R3(@NotNull JavaPlugin plugin) {
        super(plugin);
        this.registry = new CustomNbtTypeRegistry_1_8_R3();
    }

    @Override
    public @NotNull String getLocalizedName(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null && itemMeta.hasDisplayName()) {
            return itemMeta.getDisplayName();
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName();
    }

    @Override
    public @NotNull String getKeyString(@NotNull ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        Item nmsItem = nmsStack.getItem();

        MinecraftKey nmsRegistryKey = Item.REGISTRY.c(nmsItem);
        if (nmsRegistryKey == null) {
            return "minecraft:air";
        }

        return nmsRegistryKey.toString();
    }

    @Override
    public @NotNull String toNBT(@NotNull ItemStack item) {
        NBTTagCompound tag = new NBTTagCompound();
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.save(tag);
        return tag.toString();
    }

    @Override
    public @NotNull ItemStack fromNBT(@NotNull String string) {
        try {
            NBTTagCompound tag = MojangsonParser.parse(string);
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = net.minecraft.server.v1_8_R3.ItemStack.createStack(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(MojangsonParseException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an ItemStack:", ex);
            logger.warning("The item will be replaced with air.");
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public @NotNull String toBase64String(@NotNull ItemStack item) {
        NBTTagCompound tag = new NBTTagCompound();
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.save(tag);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            NBTCompressedStreamTools.a(tag, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(byteArray);
        } catch (IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to encode an item to a string:", ex);
            logger.warning("The item will not be saved properly.");
            return "";
        }
    }

    @Override
    public @NotNull ItemStack fromBase64String(@NotNull String string) {
        if (string.isEmpty()) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Decoded an empty string to air.");
            return new ItemStack(Material.AIR);
        }

        Decoder decoder = Base64.getDecoder();
        byte[] byteArray = decoder.decode(string);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray)) {
            NBTTagCompound tag = NBTCompressedStreamTools.a(inputStream);
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = net.minecraft.server.v1_8_R3.ItemStack.createStack(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to encode an item from a string:", ex);
            logger.warning("The item will be loaded as air.");
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public @NotNull CustomNbtContainer createNewCustomNbtContainer() {
        return new CustomNbtContainer_1_8_R3(this.registry);
    }

    @Override
    public @NotNull CustomNbtContainer getCustomNbt(@NotNull ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = createNBT(nmsItem);

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound compound = tag.getCompound(pluginName);
        return this.registry.extract(CustomNbtContainer.class, compound);
    }

    @Override
    public @NotNull ItemStack setCustomNbt(@NotNull ItemStack item, @NotNull CustomNbtContainer container) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = createNBT(nmsItem);

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTBase wrap = this.registry.wrap(CustomNbtContainer.class, container);
        tag.set(pluginName, wrap);

        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private @NotNull NBTTagCompound createNBT(@NotNull net.minecraft.server.v1_8_R3.ItemStack nmsItem) {
        if (nmsItem.hasTag()) {
            NBTTagCompound tag = nmsItem.getTag();
            if (tag != null) {
                return tag;
            }
        }

        return new NBTTagCompound();
    }

    @Override
    public @Nullable Component getDisplayName(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return null;
        }

        String legacyName = itemMeta.getDisplayName();
        return ComponentHelper.toComponent(legacyName);
    }

    @Override
    public @NotNull ItemStack setDisplayName(@NotNull ItemStack item, @Nullable Component displayName) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return item;
        }

        if (displayName == null) {
            itemMeta.setDisplayName(null);
        } else {
            String legacyName = ComponentHelper.toLegacy(displayName);
            itemMeta.setDisplayName(legacyName);
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public @Nullable List<Component> getLore(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null || !itemMeta.hasLore()) {
            return null;
        }

        List<String> legacyLore = itemMeta.getLore();
        return legacyLore.stream().map(ComponentHelper::toComponent).collect(Collectors.toList());
    }

    @Override
    public @NotNull ItemStack setLore(@NotNull ItemStack item, @Nullable List<Component> lore) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return item;
        }

        if (lore == null) {
            itemMeta.setLore(null);
        } else {
            List<String> legacyLore = lore.stream().map(ComponentHelper::toLegacy).collect(Collectors.toList());
            itemMeta.setLore(legacyLore);
        }

        item.setItemMeta(itemMeta);
        return item;
    }
}
