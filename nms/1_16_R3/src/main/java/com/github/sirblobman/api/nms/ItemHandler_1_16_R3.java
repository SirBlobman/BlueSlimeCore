package com.github.sirblobman.api.nms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.modern.CustomNbtPersistentDataContainerWrapper;
import com.github.sirblobman.api.nbt.modern.PersistentDataConverter;
import com.github.sirblobman.api.utility.ItemUtility;

import org.jetbrains.annotations.Nullable;

public class ItemHandler_1_16_R3 extends ItemHandler {
    public ItemHandler_1_16_R3(JavaPlugin plugin) {
        super(plugin);
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
        return nmsItem.getName().getText();
    }

    @Override
    public String getKeyString(org.bukkit.inventory.ItemStack item) {
        if (item == null) {
            return "minecraft:air";
        }

        Material material = item.getType();
        NamespacedKey key = material.getKey();
        return key.toString();
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
            ItemStack nmsItem = ItemStack.a(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (CommandSyntaxException ex) {
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

        ItemStack nmsItem = ItemStack.a(nbtTagCompound);
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
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        IChatBaseComponent nmsComponent = getNmsComponent(displayName);
        nmsItem.a(nmsComponent);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public org.bukkit.inventory.ItemStack setLore(org.bukkit.inventory.ItemStack item, List<Component> lore) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nmsTag = nmsItem.getOrCreateTag();
        NBTTagCompound displayTag = nmsTag.getCompound("display");

        NBTTagList jsonList = getJsonList(lore);
        displayTag.set("Lore", jsonList);
        nmsTag.set("display", displayTag);

        nmsItem.setTag(nmsTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private String getJsonComponent(Component adventure) {
        return ComponentHelper.toGson(adventure);
    }

    private IChatBaseComponent getNmsComponent(Component adventure) {
        String json = ComponentHelper.toGson(adventure);
        return ChatSerializer.a(json);
    }

    private NBTTagList getJsonList(List<Component> adventureList) {
        NBTTagList jsonList = new NBTTagList();
        for (Component adventure : adventureList) {
            String json = getJsonComponent(adventure);
            NBTTagString stringTag = NBTTagString.a(json);
            jsonList.add(stringTag);
        }

        return jsonList;
    }

    @Override
    public CustomNbtContainer createNewCustomNbtContainer() {
        return getCustomNbt(new org.bukkit.inventory.ItemStack(Material.BARRIER));
    }

    @Override
    public org.bukkit.inventory.ItemStack setCustomNbt(org.bukkit.inventory.ItemStack item,
                                                       CustomNbtContainer tag) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return null;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (tag instanceof CustomNbtPersistentDataContainerWrapper) {
            CustomNbtPersistentDataContainerWrapper wrapper = (CustomNbtPersistentDataContainerWrapper) tag;
            PersistentDataContainer internalContainer = wrapper.getContainer();

            JavaPlugin plugin = getPlugin();
            NamespacedKey pluginKey = new NamespacedKey(plugin, plugin.getName().toLowerCase(Locale.US));
            container.set(pluginKey, PersistentDataType.TAG_CONTAINER, internalContainer);
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public CustomNbtContainer getCustomNbt(org.bukkit.inventory.ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer dataContainer = createNBT(itemMeta);
        if (dataContainer == null) {
            return null;
        }

        JavaPlugin plugin = getPlugin();
        return PersistentDataConverter.convertContainer(plugin, dataContainer);
    }

    @Nullable
    private PersistentDataContainer createNBT(ItemMeta itemMeta) {
        if (itemMeta == null) {
            return null;
        }

        JavaPlugin plugin = getPlugin();
        NamespacedKey pluginKey = new NamespacedKey(plugin, plugin.getName().toLowerCase(Locale.US));
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();

        if (dataContainer.has(pluginKey, PersistentDataType.TAG_CONTAINER)) {
            return dataContainer.get(pluginKey, PersistentDataType.TAG_CONTAINER);
        }

        PersistentDataAdapterContext context = dataContainer.getAdapterContext();
        PersistentDataContainer newContainer = context.newPersistentDataContainer();
        dataContainer.set(pluginKey, PersistentDataType.TAG_CONTAINER, newContainer);
        return newContainer;
    }
}
