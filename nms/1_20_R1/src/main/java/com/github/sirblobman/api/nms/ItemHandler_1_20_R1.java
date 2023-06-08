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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.chat.IChatMutableComponent;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers.NBT;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.modern.CustomNbtPersistentDataContainerWrapper;
import com.github.sirblobman.api.nbt.modern.PersistentDataConverter;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.serializer.gson.GsonComponentSerializer;

public final class ItemHandler_1_20_R1 extends ItemHandler {
    public ItemHandler_1_20_R1(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull String getLocalizedName(@NotNull org.bukkit.inventory.ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        IChatBaseComponent hoverName = nmsItem.y();
        return CraftChatMessage.fromComponent(hoverName);
    }

    @Override
    public @NotNull String getKeyString(@NotNull org.bukkit.inventory.ItemStack item) {
        Material material = item.getType();
        NamespacedKey registryKey = material.getKey();
        return registryKey.toString();
    }

    @Override
    public @NotNull String toNBT(@NotNull org.bukkit.inventory.ItemStack item) {
        NBTTagCompound tag = new NBTTagCompound();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.b(tag);
        return tag.toString();
    }

    @Override
    public @NotNull org.bukkit.inventory.ItemStack fromNBT(@NotNull String string) {
        try {
            StringReader stringReader = new StringReader(string);
            MojangsonParser tagParser = new MojangsonParser(stringReader);
            NBTTagCompound tag = tagParser.f();

            net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.a(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(CommandSyntaxException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an ItemStack:", ex);
            logger.warning("The item will be replaced with air.");
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }
    }

    @Override
    public @NotNull String toBase64String(@NotNull org.bukkit.inventory.ItemStack item) {
        NBTTagCompound tag = new NBTTagCompound();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.b(tag);

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
    public @NotNull org.bukkit.inventory.ItemStack fromBase64String(@NotNull String string) {
        if (string.isEmpty()) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Decoded an empty string to air.");
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }

        Decoder decoder = Base64.getDecoder();
        byte[] byteArray = decoder.decode(string);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray)) {
            NBTTagCompound tag = NBTCompressedStreamTools.a(inputStream);
            net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.a(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to encode an item from a string:", ex);
            logger.warning("The item will be loaded as air.");
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }
    }

    @Override
    public @NotNull CustomNbtContainer createNewCustomNbtContainer() {
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(Material.BARRIER);
        return getCustomNbt(item);
    }

    @Override
    public @NotNull CustomNbtContainer getCustomNbt(@NotNull org.bukkit.inventory.ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return createNewCustomNbtContainer();
        }

        JavaPlugin plugin = getPlugin();
        PersistentDataContainer dataContainer = createNBT(item);
        return PersistentDataConverter.convertContainer(plugin, dataContainer);
    }

    @Override
    public @NotNull org.bukkit.inventory.ItemStack setCustomNbt(@NotNull org.bukkit.inventory.ItemStack item, @NotNull CustomNbtContainer container) {
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

    private @NotNull PersistentDataContainer createNBT(@NotNull org.bukkit.inventory.ItemStack item) {
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
    public @Nullable Component getDisplayName(@NotNull org.bukkit.inventory.ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (nmsItem.A()) {
            IChatBaseComponent component = nmsItem.y();
            return convert(component);
        }

        return null;
    }

    @Override
    public @NotNull org.bukkit.inventory.ItemStack setDisplayName(@NotNull org.bukkit.inventory.ItemStack item, @Nullable Component displayName) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        if (displayName == null) {
            nmsItem.z();
        } else {
            IChatBaseComponent nmsComponent = convert(displayName);
            nmsItem.a(nmsComponent);
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public @Nullable List<Component> getLore(@NotNull org.bukkit.inventory.ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        if (!nmsItem.u()) {
            return null;
        }

        NBTTagCompound tag = nmsItem.w();
        if (!tag.b("display", NBT.TAG_COMPOUND)) {
            return null;
        }

        NBTTagCompound display = tag.p("display");
        if (!display.b("Lore", NBT.TAG_LIST)) {
            return null;
        }

        NBTTagList list = display.c("Lore", CraftMagicNumbers.NBT.TAG_STRING);
        List<String> loreJson = new ArrayList<>(list.size());
        for (int index = 0; index < list.size(); index++) {
            String line = list.j(index);
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
    public @NotNull org.bukkit.inventory.ItemStack setLore(@NotNull org.bukkit.inventory.ItemStack item, @Nullable List<Component> lore) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.w();
        NBTTagCompound display = tag.p("display");

        if (lore == null) {
            display.r("Lore");
        } else {
            GsonComponentSerializer serializer = GsonComponentSerializer.gson();
            NBTTagList jsonList = new NBTTagList();
            for (Component component : lore) {
                String json = serializer.serialize(component);
                NBTTagString stringTag = NBTTagString.a(json);
                jsonList.add(stringTag);
            }

            display.a("Lore", jsonList);
        }

        tag.a("display", display);
        nmsItem.c(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private @NotNull Component convert(@NotNull IChatBaseComponent component) {
        String json = ChatSerializer.a(component);
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.deserialize(json);
    }

    private @NotNull IChatBaseComponent convert(@NotNull Component component) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        String json = serializer.serialize(component);

        IChatMutableComponent nmsComponent = ChatSerializer.a(json);
        return (nmsComponent != null ? nmsComponent : IChatBaseComponent.b(json));
    }
}
