package com.github.sirblobman.api.nms;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Locale;
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

import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.modern.CustomNbtPersistentDataContainerWrapper;
import com.github.sirblobman.api.nbt.modern.PersistentDataConverter;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.paper.ComponentConverter;
import com.github.sirblobman.api.shaded.adventure.text.Component;


public final class ItemHandler_Paper extends ItemHandler {
    private final ItemHandler backup;

    public ItemHandler_Paper(@NotNull JavaPlugin plugin, @NotNull ItemHandler backup) {
        super(plugin);
        this.backup = backup;

        Logger logger = getLogger();
        logger.info("Using Paper ItemHandler with NMS backup for NBT.");
    }

    private @NotNull ItemHandler getBackup() {
        return this.backup;
    }

    @Override
    public @NotNull String getLocalizedName(@NotNull ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null && itemMeta.hasDisplayName()) {
            net.kyori.adventure.text.Component paperDisplayName = itemMeta.displayName();
            if (paperDisplayName != null) {
                Component displayName = ComponentConverter.normalToShaded(paperDisplayName);
                return ComponentHelper.toPlain(displayName);
            }
        }

        String languageName = item.getI18NDisplayName();
        return (languageName != null ? languageName : item.getTranslationKey());
    }

    @Override
    public @NotNull String getKeyString(@NotNull ItemStack item) {
        Material material = item.getType();
        NamespacedKey key = material.getKey();
        return key.toString();
    }

    @Override
    public @NotNull String toNBT(@NotNull ItemStack item) {
        ItemHandler backup = getBackup();
        return backup.toNBT(item);
    }

    @Override
    public @NotNull ItemStack fromNBT(@NotNull String string) {
        ItemHandler backup = getBackup();
        return backup.fromNBT(string);
    }

    @Override
    public @NotNull String toBase64String(@NotNull ItemStack item) {
        byte[] itemBytes = item.serializeAsBytes();
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(itemBytes);
    }

    @Override
    public @NotNull ItemStack fromBase64String(@NotNull String string) {
        Decoder decoder = Base64.getDecoder();
        byte[] byteArray = decoder.decode(string);
        return ItemStack.deserializeBytes(byteArray);
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
        if (container instanceof CustomNbtPersistentDataContainerWrapper) {
            CustomNbtPersistentDataContainerWrapper wrapper = (CustomNbtPersistentDataContainerWrapper) container;
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
        if (!item.hasItemMeta()) {
            return null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return null;
        }

        net.kyori.adventure.text.Component paperDisplayName = itemMeta.displayName();
        if (paperDisplayName == null) {
            return null;
        }

        return ComponentConverter.normalToShaded(paperDisplayName);
    }

    @Override
    public @NotNull ItemStack setDisplayName(@NotNull ItemStack item, @Nullable Component displayName) {
        if (displayName == null) {
            item.editMeta(meta -> meta.displayName(null));
            return item;
        }

        net.kyori.adventure.text.Component paperDisplayName = ComponentConverter.shadedToNormal(displayName);
        item.editMeta(meta -> meta.displayName(paperDisplayName));
        return item;
    }

    @Override
    public @Nullable List<Component> getLore(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null || !itemMeta.hasLore()) {
            return null;
        }

        List<net.kyori.adventure.text.Component> paperLore = itemMeta.lore();
        if (paperLore == null) {
            return null;
        }

        List<Component> lore = new ArrayList<>();
        for (net.kyori.adventure.text.Component paperLine : paperLore) {
            Component line = ComponentConverter.normalToShaded(paperLine);
            lore.add(line);
        }

        return lore;
    }

    @Override
    public @NotNull ItemStack setLore(@NotNull ItemStack item, @Nullable List<Component> lore) {
        if (lore == null) {
            item.editMeta(meta -> meta.lore(null));
            return item;
        }

        List<net.kyori.adventure.text.Component> paperLore = new ArrayList<>();
        for (Component line : lore) {
            net.kyori.adventure.text.Component paperLine = ComponentConverter.shadedToNormal(line);
            paperLore.add(paperLine);
        }

        item.editMeta(meta -> meta.lore(paperLore));
        return item;
    }
}
