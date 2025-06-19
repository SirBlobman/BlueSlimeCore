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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
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
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.WorldData;
import org.bukkit.craftbukkit.v1_21_R5.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R5.CraftServer;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftChatMessage;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

public final class ItemHandler_1_21_R5 extends ItemHandler {
    public ItemHandler_1_21_R5(@NotNull JavaPlugin plugin) {
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
        if (material.isRegistered()) {
            NamespacedKey key = material.getKeyOrNull();
            if (key != null) {
                return key.toString();
            }
        }

        return "minecraft:air";
    }

    @Override
    public @NotNull String toNBT(@NotNull ItemStack item) {
        RegistryAccess registry = CraftRegistry.getMinecraftRegistry();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        JavaPlugin plugin = getPlugin();
        ProblemReporter problemReporter = new ProblemReporter_1_21_R5(plugin);
        TagValueOutput tagValueOutput = TagValueOutput.createWithContext(problemReporter, registry);
        tagValueOutput.store("item", net.minecraft.world.item.ItemStack.CODEC, nmsItem);

        CompoundTag output = tagValueOutput.buildResult();
        Tag tag = output.getCompoundOrEmpty("item");
        return tag.toString();
    }

    @Override
    public @NotNull ItemStack fromNBT(@NotNull String string) {
        try {
            CompoundTag tag = fixData(TagParser.parseCompoundFully(string));
            RegistryAccess registry = CraftRegistry.getMinecraftRegistry();

            DynamicOps<Tag> nbtOps = registry.createSerializationContext(NbtOps.INSTANCE);
            DataResult<Pair<net.minecraft.world.item.ItemStack, Tag>> decode = net.minecraft.world.item.ItemStack.CODEC.decode(nbtOps, tag);
            Pair<net.minecraft.world.item.ItemStack, Tag> pair = decode.getOrThrow();

            net.minecraft.world.item.ItemStack nmsItem = pair.getFirst();
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
        RegistryAccess registry = CraftRegistry.getMinecraftRegistry();
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        JavaPlugin plugin = getPlugin();
        ProblemReporter problemReporter = new ProblemReporter_1_21_R5(plugin);
        TagValueOutput tagValueOutput = TagValueOutput.createWithContext(problemReporter, registry);
        tagValueOutput.store("item", net.minecraft.world.item.ItemStack.CODEC, nmsItem);

        CompoundTag output = tagValueOutput.buildResult();
        CompoundTag tag = output.getCompoundOrEmpty("item");

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
            RegistryAccess registry = CraftRegistry.getMinecraftRegistry();
            CompoundTag tag = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
            CompoundTag fixedTag = fixData(tag);

            DynamicOps<Tag> nbtOps = registry.createSerializationContext(NbtOps.INSTANCE);
            DataResult<Pair<net.minecraft.world.item.ItemStack, Tag>> decode = net.minecraft.world.item.ItemStack.CODEC.decode(nbtOps, fixedTag);
            Pair<net.minecraft.world.item.ItemStack, Tag> pair = decode.getOrThrow();

            net.minecraft.world.item.ItemStack nmsItem = pair.getFirst();
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
        RegistryAccess registry = CraftRegistry.getMinecraftRegistry();
        String json = CraftChatMessage.ChatSerializer.toJson(component, registry);
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        return serializer.deserialize(json);
    }

    private @NotNull net.minecraft.network.chat.Component convert(@NotNull Component component) {
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        String json = serializer.serialize(component);

        RegistryAccess registry = CraftRegistry.getMinecraftRegistry();
        MutableComponent nmsComponent = CraftChatMessage.ChatSerializer.fromJson(json, registry);
        if (nmsComponent != null) {
            return nmsComponent;
        }

        return net.minecraft.network.chat.Component.literal(json);
    }

    private int getCurrentDataVersion() {
        Server server = Bukkit.getServer();
        if (server instanceof CraftServer craftServer) {
            DedicatedPlayerList handle = craftServer.getHandle();
            DedicatedServer dedicatedServer = handle.getServer();
            WorldData worldData = dedicatedServer.getWorldData();
            return worldData.getVersion();
        }

        return -1;
    }

    private int getDataVersion(CompoundTag nbt) {
        int defaultValue = getCurrentDataVersion();
        if(nbt.contains("DataVersion")) {
            return nbt.getIntOr("DataVersion", -1);
        }

        return defaultValue;
    }

    private CompoundTag fixData(CompoundTag nbt) {
        int currentVersion = getCurrentDataVersion();
        int nbtVersion = getDataVersion(nbt);

        // NBT from future version, can't fix.
        if (nbtVersion > currentVersion) {
            String message = String.format(Locale.US, "Can't fix data of newer NBT version. %s > %s.",
                    nbtVersion, currentVersion);
            throw new IllegalStateException(message);
        }

        // Same version, nothing to fix
        if (currentVersion == nbtVersion) {
            return nbt;
        }

        Server server = Bukkit.getServer();
        if (server instanceof CraftServer craftServer) {
            DedicatedPlayerList handle = craftServer.getHandle();
            DedicatedServer dedicatedServer = handle.getServer();
            DataFixer dataFixer = dedicatedServer.getFixerUpper();
            return (CompoundTag) dataFixer.update(References.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, nbt), nbtVersion, currentVersion).getValue();
        }

        throw new IllegalStateException("The server is somehow not a CraftServer?!");
    }
}
