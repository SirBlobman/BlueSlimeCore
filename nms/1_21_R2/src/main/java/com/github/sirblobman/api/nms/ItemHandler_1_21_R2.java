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

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public final class ItemHandler_1_21_R2 extends ItemHandler {
    public ItemHandler_1_21_R2(@NotNull JavaPlugin plugin) {
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
        MinecraftServer server = MinecraftServer.getServer();
        RegistryAccess.Frozen frozenRegistry = server.registryAccess();

        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        Tag tag = nmsItem.save(frozenRegistry);
        return tag.toString();
    }

    @Override
    public @NotNull ItemStack fromNBT(@NotNull String string) {
        try {
            StringReader stringReader = new StringReader(string);
            TagParser tagParser = new TagParser(stringReader);
            CompoundTag tag = tagParser.readStruct();

            MinecraftServer server = MinecraftServer.getServer();
            RegistryAccess.Frozen frozenRegistry = server.registryAccess();
            net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.parse(frozenRegistry, tag)
                    .orElseThrow();
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
        MinecraftServer server = MinecraftServer.getServer();
        RegistryAccess.Frozen frozenRegistry = server.registryAccess();

        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        Tag tag = nmsItem.save(frozenRegistry);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            NbtIo.writeCompressed((CompoundTag) tag, outputStream);
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
            CompoundTag tag = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());

            MinecraftServer server = MinecraftServer.getServer();
            RegistryAccess.Frozen frozenRegistry = server.registryAccess();
            net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.parse(frozenRegistry, tag)
                    .orElseThrow();
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
        if (nmsItem.has(DataComponents.CUSTOM_NAME)) {
            net.minecraft.network.chat.Component component = nmsItem.getHoverName();
            return convert(component);
        }

        return null;
    }

    @Override
    public @NotNull ItemStack setDisplayName(@NotNull ItemStack item, @Nullable Component displayName) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        if (displayName == null) {
            if (nmsItem.has(DataComponents.CUSTOM_NAME)) {
                nmsItem.remove(DataComponents.CUSTOM_NAME);
            }
        } else {
            net.minecraft.network.chat.Component nmsComponent = convert(displayName);
            nmsItem.set(DataComponents.CUSTOM_NAME, nmsComponent);
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public @Nullable List<Component> getLore(@NotNull ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (!nmsItem.has(DataComponents.LORE)) {
            return null;
        }

        ItemLore itemLore = nmsItem.get(DataComponents.LORE);
        if (itemLore == null) {
            return null;
        }

        List<net.minecraft.network.chat.Component> nmsLines = itemLore.lines();
        List<Component> adventureLines = nmsLines.stream().map(this::convert).toList();
        return new ArrayList<>(adventureLines);
    }

    @Override
    public @NotNull ItemStack setLore(@NotNull ItemStack item, @Nullable List<Component> lore) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        if (lore == null) {
            if (nmsItem.has(DataComponents.LORE)) {
                nmsItem.remove(DataComponents.LORE);
            }
        } else {
            List<net.minecraft.network.chat.Component> nmsLines = new ArrayList<>(lore.stream().map(this::convert)
                    .toList());
            ItemLore itemLore = new ItemLore(nmsLines);
            nmsItem.set(DataComponents.LORE, itemLore);
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private @NotNull Component convert(@NotNull net.minecraft.network.chat.Component  component) {
        MinecraftServer server = MinecraftServer.getServer();
        RegistryAccess.Frozen frozenRegistry = server.registryAccess();
        String json = Serializer.toJson(component, frozenRegistry);
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.deserialize(json);
    }

    private @NotNull net.minecraft.network.chat.Component convert(@NotNull Component component) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        String json = serializer.serialize(component);

        MinecraftServer server = MinecraftServer.getServer();
        RegistryAccess.Frozen frozenRegistry = server.registryAccess();
        MutableComponent nmsComponent = Serializer.fromJson(json, frozenRegistry);
        if (nmsComponent != null) {
            return nmsComponent;
        }

        return net.minecraft.network.chat.Component.literal(json);
    }
}
