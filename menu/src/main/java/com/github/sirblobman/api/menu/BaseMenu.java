package com.github.sirblobman.api.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.item.ComponentItemBuilder;
import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.item.SkullBuilder;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.utility.MessageUtility;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.Nullable;

public abstract class BaseMenu implements IMenu {
    /**
     * @return The head handler for the current Bukkit version if there is one.
     */
    @Nullable
    public HeadHandler getHeadHandler() {
        return null;
    }

    /**
     * @return The item handler for the current Bukkit version if there is one.
     */
    @Nullable
    public ItemHandler getItemHandler() {
        return null;
    }

    /**
     * @return The language manager if your plugin has one.
     */
    @Nullable
    public LanguageManager getLanguageManager() {
        return null;
    }

    /**
     * @param size  The size of the inventory. Must be five for a hopper menu or a non-zero multiple of
     *              nine for a chest menu.
     * @param title The title of the GUI.
     *              (legacy color codes with the '&amp;' symbol will be translated automatically)
     * @return An empty {@link Inventory} instance with this menu instance as its holder.
     */
    public Inventory getInventory(int size, String title) {
        String realTitle = MessageUtility.color(title);

        if (size == 5) {
            return Bukkit.createInventory(this, InventoryType.HOPPER, realTitle);
        }

        if (size < 9) {
            throw new IllegalArgumentException("size must be equal to 5 or at least 9");
        }

        if (size > 54) {
            throw new IllegalArgumentException("size cannot be more than 54");
        }

        if (size % 9 != 0) {
            throw new IllegalArgumentException("size must be equal to 5 or divisible by 9");
        }

        return Bukkit.createInventory(this, size, realTitle);
    }

    /**
     * Load an ItemStack from a configuration file or section.
     *
     * @param config The configuration or section to load the item from.
     * @param path   The path in the configuration or section.
     * @return An {@link ItemStack} read from the section, or {@code null} if one could not be read.
     */
    @Nullable
    protected final ItemStack loadItemStack(ConfigurationSection config, String path) {
        if (config.isItemStack(path)) {
            return config.getItemStack(path);
        }

        ConfigurationSection section = config.getConfigurationSection(path);
        return loadItemStack(section);
    }

    private ItemStack loadItemStack(ConfigurationSection section) {
        if (section == null) {
            return null;
        }

        String materialName = section.getString("material");
        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(materialName);
        if (!optionalMaterial.isPresent()) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.warning("Unknown material name '" + materialName + "'.");
            return null;
        }

        XMaterial material = optionalMaterial.get();
        ItemBuilder builder = new ItemBuilder(material);
        builder = checkSkull(builder, material, section);
        builder = checkNameAndLore(builder, section);

        int amount = section.getInt("quantity", 1);
        builder.withAmount(amount);

        int damage = section.getInt("damage", 0);
        builder.withDamage(damage);

        Integer model = (section.isSet("model") ? section.getInt("model") : null);
        builder.withModel(model);

        if (section.getBoolean("glowing")) {
            builder.withGlowing();
        }

        return builder.build();
    }

    private ItemBuilder checkNameAndLore(ItemBuilder builder, ConfigurationSection section) {
        ItemHandler itemHandler = getItemHandler();
        LanguageManager languageManager = getLanguageManager();

        String displayNameString = section.getString("display-name");
        if (displayNameString != null) {
            if (itemHandler != null && languageManager != null) {
                MiniMessage miniMessage = languageManager.getMiniMessage();
                Component displayName = miniMessage.deserialize(displayNameString);

                ItemStack oldItem = builder.build();
                ComponentItemBuilder newBuilder = new ComponentItemBuilder(itemHandler, oldItem);
                newBuilder.withName(displayName);
                builder = newBuilder.asItemBuilder();
            } else {
                String displayName = MessageUtility.color(displayNameString);
                return builder.withName(displayName);
            }
        }

        List<String> loreStringList = section.getStringList("lore");
        if (!loreStringList.isEmpty()) {
            if (itemHandler != null && languageManager != null) {
                MiniMessage miniMessage = languageManager.getMiniMessage();
                List<Component> lore = new ArrayList<>();

                for (String lineString : loreStringList) {
                    Component line = miniMessage.deserialize(lineString);
                    lore.add(line);
                }

                ItemStack oldItem = builder.build();
                ComponentItemBuilder newBuilder = new ComponentItemBuilder(itemHandler, oldItem);
                newBuilder.withLore(lore);
                builder = newBuilder.asItemBuilder();
            } else {
                List<String> lore = MessageUtility.colorList(loreStringList);
                builder.withLore(lore);
            }
        }

        return builder;
    }

    private ItemBuilder checkSkull(ItemBuilder builder, XMaterial material, ConfigurationSection section) {
        HeadHandler headHandler = getHeadHandler();
        if (material != XMaterial.PLAYER_HEAD || headHandler == null) {
            return builder;
        }

        String texture = section.getString("texture");
        if (texture != null) {
            SkullBuilder skullBuilder = new SkullBuilder(headHandler);
            return skullBuilder.withTextureBase64(texture);
        }

        String textureUrl = section.getString("texture-url");
        if (textureUrl != null) {
            SkullBuilder skullBuilder = new SkullBuilder(headHandler);
            return skullBuilder.withTextureUrl(textureUrl);
        }

        String username = section.getString("skull-owner");
        if (username != null) {
            SkullBuilder skullBuilder = new SkullBuilder(headHandler);
            return skullBuilder.withOwner(username);
        }

        return builder;
    }
}
