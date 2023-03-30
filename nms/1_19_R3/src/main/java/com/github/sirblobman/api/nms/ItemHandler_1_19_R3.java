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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftChatMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.modern.CustomNbtPersistentDataContainerWrapper;
import com.github.sirblobman.api.nbt.modern.PersistentDataConverter;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.utility.ItemUtility;

import org.jetbrains.annotations.Nullable;

public final class ItemHandler_1_19_R3 extends ItemHandler {
    public ItemHandler_1_19_R3(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack item) {
        ItemStack nmsItem = ItemStack.EMPTY;
        if (!ItemUtility.isAir(item)) {
            nmsItem = CraftItemStack.asNMSCopy(item);
        }

        net.minecraft.network.chat.Component hoverName = nmsItem.getHoverName();
        return CraftChatMessage.fromComponent(hoverName);
    }

    @Override
    public String getKeyString(org.bukkit.inventory.ItemStack item) {
        Material material = Material.AIR;
        if (!ItemUtility.isAir(item)) {
            material = item.getType();
        }

        NamespacedKey key = material.getKey();
        return key.toString();
    }

    @Override
    public String toNBT(org.bukkit.inventory.ItemStack item) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag compoundTag = new CompoundTag();
        nmsItem.save(compoundTag);
        return compoundTag.toString();
    }

    @Override
    public org.bukkit.inventory.ItemStack fromNBT(String string) {
        try {
            StringReader stringReader = new StringReader(string);
            TagParser tagParser = new TagParser(stringReader);
            CompoundTag compoundTag = tagParser.readStruct();

            ItemStack nmsItem = ItemStack.of(compoundTag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (CommandSyntaxException ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item:", ex);
            logger.log(Level.WARNING, "returning AIR....");
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }
    }

    @Override
    public org.bukkit.inventory.ItemStack fromBase64String(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }

        CompoundTag compoundTag;
        byte[] decode = Base64.getDecoder().decode(string);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);

        try {
            compoundTag = NbtIo.readCompressed(inputStream);
        } catch (Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to decode an item from a string because an error occurred:", ex);
            return null;
        }

        ItemStack nmsItem = ItemStack.of(compoundTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public String toBase64String(org.bukkit.inventory.ItemStack item) {
        if (ItemUtility.isAir(item)) {
            return null;
        }

        CompoundTag compoundTag = new CompoundTag();
        CraftItemStack.asNMSCopy(item).save(compoundTag);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            NbtIo.writeCompressed(compoundTag, outputStream);
        } catch (Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to encode an item to a string because an error occurred:", ex);
            return null;
        }

        byte[] encode = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(encode);
    }

    @Override
    public org.bukkit.inventory.ItemStack setDisplayName(org.bukkit.inventory.ItemStack item,
                                                         Component displayName) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        net.minecraft.network.chat.Component nmsComponent = getNmsComponent(displayName);
        nmsItem.setHoverName(nmsComponent);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public org.bukkit.inventory.ItemStack setLore(org.bukkit.inventory.ItemStack item,
                                                  List<Component> lore) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag nmsTag = nmsItem.getOrCreateTag();
        CompoundTag displayTag = nmsTag.getCompound("display");

        ListTag jsonList = getJsonList(lore);
        displayTag.put("Lore", jsonList);
        nmsTag.put("display", displayTag);

        nmsItem.setTag(nmsTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private String getJsonComponent(Component adventure) {
        return ComponentHelper.toGson(adventure);
    }

    private net.minecraft.network.chat.Component getNmsComponent(Component adventure) {
        String json = ComponentHelper.toGson(adventure);
        return Serializer.fromJson(json);
    }

    private ListTag getJsonList(List<Component> adventureList) {
        ListTag jsonList = new ListTag();
        for (Component adventure : adventureList) {
            String json = getJsonComponent(adventure);
            StringTag stringTag = StringTag.valueOf(json);
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
                                                       CustomNbtContainer customNbtContainer) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return null;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (customNbtContainer instanceof CustomNbtPersistentDataContainerWrapper wrapper) {
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
