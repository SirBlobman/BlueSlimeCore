package com.github.sirblobman.api.nms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.modern.CustomNbtPersistentDataContainerWrapper;
import com.github.sirblobman.api.nbt.modern.PersistentDataConverter;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.serializer.gson.GsonComponentSerializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.chat.MutableComponent;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public final class ItemHandler_1_20_R1 extends ItemHandler {
    public ItemHandler_1_20_R1(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull String getLocalizedName(@NotNull ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        net.minecraft.network.chat.Component hoverName = nmsItem.getHoverName();
        return CraftChatMessage.fromComponent(hoverName);
    }

    @Override
    public @NotNull String getKeyString(@NotNull ItemStack item) {
        Material material = item.getType();
        NamespacedKey registryKey = material.getKey();
        return registryKey.toString();
    }

    @Override
    public @NotNull String toNBT(@NotNull ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag tag = new CompoundTag();
        nmsItem.save(tag);
        return tag.toString();
    }

    @Override
    public @NotNull ItemStack fromNBT(@NotNull String string) {
        try {
            StringReader stringReader = new StringReader(string);
            TagParser tagParser = new TagParser(stringReader);
            CompoundTag tag = tagParser.readStruct();

            net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.of(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(CommandSyntaxException | NoSuchElementException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an ItemStack:", ex);
            logger.warning("The item will be replaced with air.");
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public @NotNull String toBase64String(@NotNull ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag tag = new CompoundTag();
        nmsItem.save(tag);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            NbtIo.writeCompressed(tag, outputStream);
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
            CompoundTag tag = NbtIo.readCompressed(inputStream);
            net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.of(tag);
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
        ItemStack item = new ItemStack(Material.BARRIER);
        return getCustomNbt(item);
    }

    @Override
    public @NotNull CustomNbtContainer getCustomNbt(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return createNewCustomNbtContainer();
        }

        JavaPlugin plugin = getPlugin();
        PersistentDataContainer dataContainer = createNBT(item);
        return PersistentDataConverter.convertContainer(plugin, dataContainer);
    }

    @Override
    public @NotNull ItemStack setCustomNbt(@NotNull ItemStack item, @NotNull CustomNbtContainer container) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return item;
        }

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        if (container instanceof CustomNbtPersistentDataContainerWrapper wrapper) {
            PersistentDataContainer internalContainer = wrapper.getContainer();

            JavaPlugin plugin = getPlugin();
            NamespacedKey pluginKey = new NamespacedKey(plugin, plugin.getName().toLowerCase(Locale.US));
            dataContainer.set(pluginKey, PersistentDataType.TAG_CONTAINER, internalContainer);
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    private @NotNull PersistentDataContainer createNBT(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        Validate.notNull(itemMeta, "item must not have null meta!");

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName().toLowerCase(Locale.US);
        NamespacedKey pluginKey = new NamespacedKey(plugin, pluginName);

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        if (dataContainer.has(pluginKey, PersistentDataType.TAG_CONTAINER)) {
            PersistentDataContainer subContainer = dataContainer.get(pluginKey, PersistentDataType.TAG_CONTAINER);
            return Validate.notNull(subContainer, "subContainer must not be null!");
        }

        PersistentDataAdapterContext context = dataContainer.getAdapterContext();
        PersistentDataContainer newContainer = context.newPersistentDataContainer();
        dataContainer.set(pluginKey, PersistentDataType.TAG_CONTAINER, newContainer);
        return newContainer;
    }

    @Override
    public @Nullable Component getDisplayName(@NotNull ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem.hasCustomHoverName()) {
            net.minecraft.network.chat.Component component = nmsItem.getHoverName();
            return convert(component);
        }

        return null;
    }

    @Override
    public @NotNull ItemStack setDisplayName(@NotNull ItemStack item, @Nullable Component displayName) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        if (displayName == null) {
            if (nmsItem.hasCustomHoverName()) {
                nmsItem.resetHoverName();
            }
        } else {
            net.minecraft.network.chat.Component nmsComponent = convert(displayName);
            nmsItem.setHoverName(nmsComponent);
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public @Nullable List<Component> getLore(@NotNull ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (!nmsItem.hasTag()) {
            return null;
        }

        CompoundTag tag = nmsItem.getOrCreateTag();
        if (!tag.contains("display", CraftMagicNumbers.NBT.TAG_COMPOUND)) {
            return null;
        }

        CompoundTag display = tag.getCompound("display");
        if (!display.contains("Lore", CraftMagicNumbers.NBT.TAG_LIST)) {
            return null;
        }

        ListTag list = display.getList("Lore", CraftMagicNumbers.NBT.TAG_STRING);
        List<String> loreJson = new ArrayList<>(list.size());
        for (int index = 0; index < list.size(); index++) {
            String line = list.getString(index);
            loreJson.add(line);
        }

        List<Component> componentLore = new ArrayList<>();
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        for (String json : loreJson) {
            if (json == null) {
                componentLore.add(Component.empty());
                continue;
            }

            Component component = serializer.deserialize(json);
            componentLore.add(component);
        }

        return componentLore;
    }

    @Override
    public @NotNull ItemStack setLore(@NotNull ItemStack item, @Nullable List<Component> lore) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag tag = nmsItem.getOrCreateTag();
        CompoundTag display = tag.getCompound("display");

        if (lore == null) {
            display.remove("Lore");
        } else {
            GsonComponentSerializer serializer = GsonComponentSerializer.gson();
            ListTag jsonList = new ListTag();
            for (Component component : lore) {
                String json = serializer.serialize(component);
                StringTag stringTag = StringTag.valueOf(json);
                jsonList.add(stringTag);
            }

            display.put("Lore", jsonList);
        }

        tag.put("display", display);
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private @NotNull Component convert(@NotNull net.minecraft.network.chat.Component  component) {
        String json = Serializer.toJson(component);
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.deserialize(json);
    }

    private @NotNull net.minecraft.network.chat.Component convert(@NotNull Component component) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        String json = serializer.serialize(component);

        MutableComponent nmsComponent = Serializer.fromJson(json);
        if (nmsComponent != null) {
            return nmsComponent;
        }

        return net.minecraft.network.chat.Component.literal(json);
    }
}
