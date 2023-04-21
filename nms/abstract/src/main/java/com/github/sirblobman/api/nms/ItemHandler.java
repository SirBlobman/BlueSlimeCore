package com.github.sirblobman.api.nms;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.shaded.adventure.text.Component;

/**
 * Abstract NMS ItemHandler Class
 * @author SirBlobman
 */
public abstract class ItemHandler extends Handler {
    /**
     * Create a new instance of this handler.
     * @param plugin The plugin that owns this instance.
     */
    public ItemHandler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Returns the localized name of the given ItemStack.<br/>
     * Example: {@code new ItemStack(Material.IRON_SWORD, 1)} will return a value of "Iron Sword"
     *
     * @param item the ItemStack to get the name of.
     * @return the localized name of the item.
     */
    public abstract @NotNull String getLocalizedName(@NotNull ItemStack item);

    /**
     * Returns the keyed name of the given ItemStack.<br/>
     * Example: {@code new ItemStack(Material.IRON_SWORD)} will return a value of "minecraft:iron_sword"
     *
     * @param item the ItemStack to get the key string of.
     * @return the keyed name of the item.
     */
    public abstract @NotNull String getKeyString(@NotNull ItemStack item);

    /**
     * Returns the given ItemStack as an NBT string.<br/>
     * Example: {@code new ItemStack(Material.IRON_SWORD, 1)} will return a value of
     * <pre>{id:"minecraft:iron_sword",Count:1b}</pre>
     *
     * @param item the ItemStack to get the NBT data of.
     * @return the NBT data of the item as a string.
     */
    public abstract @NotNull String toNBT(@NotNull ItemStack item);

    /**
     * Constructs an ItemStack from the given NBT data.
     * Example: <pre>{id:"minecraft:iron_sword",Count:1b}</pre> will result in an item that matches the following code:
     * {@code new ItemStack(Material.IRON_SWORD, 1)}
     *
     * @param string the NBT data of the ItemStack.
     * @return the ItemStack constructed from the NBT data,
     * or {@code new ItemStack(Material.AIR)} if the encoded data is not valid.
     */
    public abstract @NotNull ItemStack fromNBT(@NotNull String string);

    /**
     * Returns the given ItemStack as a Base64 encoded data string.
     *
     * @param item The item to encode.
     * @return the ItemStack as a Base64 encoded data string, or {@code ""} if the encoding failed.
     */
    public abstract @NotNull String toBase64String(@NotNull ItemStack item);

    /**
     * Attemps to parse a Base64 encoded data string as an ItemStack.
     *
     * @param string a Base64 encoded data string of an ItemStack.
     * @return The original ItemStack, or {@code new ItemStack(Material.AIR)} if the encoded data is not valid.
     * @see #toBase64String(ItemStack)
     */
    public abstract @NotNull ItemStack fromBase64String(@NotNull String string);

    /**
     * @return A new and empty instance of a {@link CustomNbtContainer}
     */
    public abstract @NotNull CustomNbtContainer createNewCustomNbtContainer();

    /**
     * @param item The item to check.
     * @return The custom nbt container for the item. If one is missing, it will be created.
     */
    public abstract @NotNull CustomNbtContainer getCustomNbt(@NotNull ItemStack item);

    /**
     * Creates a copy of an item and add custom NBT data to the copy.
     *
     * @param item      The item to modify.
     * @param container The nbt container.
     * @return A copy of the original item with the new NBT container.
     */
    public abstract @NotNull ItemStack setCustomNbt(@NotNull ItemStack item, @NotNull CustomNbtContainer container);

    /**
     * Gets the display name of the given ItemStack as a Component.
     *
     * @param item the ItemStack to get the display name from
     * @return the display name of the ItemStack as a Component, or null if it doesn't have one
     */
    public abstract @Nullable Component getDisplayName(@NotNull ItemStack item);

    /**
     * Sets the display name of the given ItemStack to the specified Component.
     *
     * @param item        the ItemStack to set the display name for
     * @param displayName the Component to set as the display name, or null to remove the display name
     * @return the ItemStack with the updated display name
     */
    public abstract @NotNull ItemStack setDisplayName(@NotNull ItemStack item, @Nullable Component displayName);

    /**
     * Gets the lore of the given ItemStack as a list of Components.
     *
     * @param item the ItemStack to get the lore from
     * @return the lore of the ItemStack as a list of Components, or null if it doesn't have any
     */
    public abstract @Nullable List<Component> getLore(@NotNull ItemStack item);

    /**
     * Sets the lore of the given ItemStack to the specified list of Components.
     *
     * @param item the ItemStack to set the lore for
     * @param lore the list of Components to set as the lore, or null to remove the lore
     * @return the ItemStack with the updated lore
     */
    public abstract @NotNull ItemStack setLore(@NotNull ItemStack item, @Nullable List<Component> lore);
}
