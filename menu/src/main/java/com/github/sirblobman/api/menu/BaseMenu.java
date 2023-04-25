package com.github.sirblobman.api.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.item.SkullBuilder;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.nms.HeadHandler;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.utility.MessageUtility;
import com.github.sirblobman.api.utility.paper.PaperChecker;
import com.github.sirblobman.api.utility.paper.PaperHelper;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.adventure.text.minimessage.MiniMessage;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public abstract class BaseMenu<P extends Plugin> implements IMenu<P> {
    private IMenu<P> parentMenu;

    public BaseMenu() {
        this(null);
    }

    public BaseMenu(@Nullable IMenu<P> parentMenu) {
        this.parentMenu = parentMenu;
    }

    @Override
    public @NotNull Optional<IMenu<P>> getParentMenu() {
        return Optional.ofNullable(this.parentMenu);
    }

    @Override
    public void setParentMenu(@NotNull IMenu<P> parentMenu) {
        this.parentMenu = parentMenu;
    }

    /**
     * @return The head handler for the current Bukkit version if there is one.
     */
    public @Nullable HeadHandler getHeadHandler() {
        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        if (multiVersionHandler == null) {
            return null;
        }

        return multiVersionHandler.getHeadHandler();
    }

    /**
     * @return The item handler for the current Bukkit version if there is one.
     */
    public @Nullable ItemHandler getItemHandler() {
        MultiVersionHandler multiVersionHandler = getMultiVersionHandler();
        if (multiVersionHandler == null) {
            return null;
        }

        return multiVersionHandler.getItemHandler();
    }

    /**
     * @param size The size of the inventory. Must be five for a hopper menu or a non-zero multiple of
     *             nine for a chest menu.
     * @return An empty {@link Inventory} instance with this menu instance as its holder.
     */
    public @NotNull Inventory getInventory(int size) {
        if (size == 5) {
            return Bukkit.createInventory(this, InventoryType.HOPPER);
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

        return Bukkit.createInventory(this, size);
    }

    /**
     * @param size  The size of the inventory. Must be five for a hopper menu or a non-zero multiple of
     *              nine for a chest menu.
     * @param title The title of the GUI.
     *              (legacy color codes with the '&amp;' symbol will be translated automatically)
     * @return An empty {@link Inventory} instance with this menu instance as its holder.
     */
    public @NotNull Inventory getInventory(int size, @Nullable String title) {
        if (title == null) {
            return getInventory(size);
        }

        String colorTitle = MessageUtility.color(title);

        if (size == 5) {
            return Bukkit.createInventory(this, InventoryType.HOPPER, colorTitle);
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

        return Bukkit.createInventory(this, size, colorTitle);
    }

    /**
     * @param size  The size of the inventory. Must be five for a hopper menu or a non-zero multiple of
     *              nine for a chest menu.
     * @param title The component title for the GUI.
     * @return An empty {@link Inventory} instance with this menu instance as its holder.
     */
    public @NotNull Inventory getInventory(int size, @Nullable Component title) {
        if (title == null) {
            return getInventory(size);
        }

        if (PaperChecker.hasNativeComponentSupport()) {
            return PaperHelper.createInventory(this, size, title);
        } else {
            String legacyTitle = ComponentHelper.toLegacy(title);
            return getInventory(size, legacyTitle);
        }
    }

    /**
     * Load an ItemStack from a configuration file or section.
     *
     * @param config The configuration or section to load the item from.
     * @param path   The path in the configuration or section.
     * @return An {@link ItemStack} read from the section, or {@code null} if one could not be read.
     */
    protected final @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection config, @NotNull String path) {
        if (config.isItemStack(path)) {
            return config.getItemStack(path);
        }

        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) {
            return null;
        }

        return loadItemStack(section);
    }

    private @Nullable ItemStack loadItemStack(@NotNull ConfigurationSection section) {
        String materialName = section.getString("material");
        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(materialName);
        if (!optionalMaterial.isPresent()) {
            Logger logger = getLogger();
            logger.warning("Unknown material name '" + materialName + "'.");
            return null;
        }

        XMaterial material = optionalMaterial.get();
        ItemBuilder builder = new ItemBuilder(material);
        builder = checkSkull(builder, material, section);
        builder = checkNameAndLore(builder, section);

        int amount = section.getInt("quantity", 1);
        builder = builder.withAmount(amount);

        int damage = section.getInt("damage", 0);
        builder = builder.withDamage(damage);

        Integer model = (section.isSet("model") ? section.getInt("model") : null);
        builder = builder.withModel(model);

        if (section.getBoolean("glowing")) {
            builder = builder.withGlowing();
        }

        return builder.build();
    }

    private @NotNull ItemBuilder checkNameAndLore(@NotNull ItemBuilder builder,
                                                  @NotNull ConfigurationSection section) {
        builder = checkDisplayName(builder, section);
        builder = checkLore(builder, section);
        return builder;
    }

    private @NotNull ItemBuilder checkDisplayName(@NotNull ItemBuilder builder,
                                                  @NotNull ConfigurationSection section) {
        String displayNameString = section.getString("display-name");
        if (displayNameString == null) {
            return builder;
        }

        LanguageManager languageManager = getLanguageManager();
        ItemHandler itemHandler = getItemHandler();
        if (languageManager == null || itemHandler == null) {
            String displayNameFormatted = MessageUtility.color(displayNameString);
            return builder.withName(displayNameFormatted);
        }

        MiniMessage miniMessage = languageManager.getMiniMessage();
        Component displayName = miniMessage.deserialize(displayNameString);
        return builder.withName(itemHandler, ComponentHelper.wrapNoItalics(displayName));
    }

    private @NotNull ItemBuilder checkLore(@NotNull ItemBuilder builder,
                                           @NotNull ConfigurationSection section) {
        List<String> loreString = section.getStringList("lore");
        if (loreString.isEmpty()) {
            return builder;
        }

        LanguageManager languageManager = getLanguageManager();
        ItemHandler itemHandler = getItemHandler();
        if (languageManager == null || itemHandler == null) {
            List<String> loreFormatted = MessageUtility.colorList(loreString);
            return builder.withLore(loreFormatted);
        }

        List<Component> lore = new ArrayList<>();
        MiniMessage miniMessage = languageManager.getMiniMessage();
        for (String lineString : loreString) {
            Component line = miniMessage.deserialize(lineString);
            lore.add(line);
        }

        return builder.withLore(itemHandler, ComponentHelper.wrapNoItalics(lore));
    }

    private @NotNull ItemBuilder checkSkull(@NotNull ItemBuilder builder, @NotNull XMaterial material,
                                            @NotNull ConfigurationSection section) {
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
