package com.github.sirblobman.api.core.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.core.CorePlugin;
import com.github.sirblobman.api.item.ItemBuilder;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.AbstractMenu;
import com.github.sirblobman.api.menu.button.AbstractButton;
import com.github.sirblobman.api.menu.button.ExitButton;
import com.github.sirblobman.api.menu.button.OpenMenuButton;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class ConfigurationSelectorMenu extends AbstractMenu {
    private final CorePlugin plugin;

    public ConfigurationSelectorMenu(@NotNull CorePlugin plugin, @NotNull Player player) {
        super(plugin, player);
        this.plugin = plugin;
    }

    private @NotNull CorePlugin getCorePlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull MultiVersionHandler getMultiVersionHandler() {
        CorePlugin plugin = getCorePlugin();
        return plugin.getMultiVersionHandler();
    }

    @Override
    public @NotNull LanguageManager getLanguageManager() {
        CorePlugin plugin = getCorePlugin();
        return plugin.getLanguageManager();
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        switch (slot) {
            case 0:
                return getConfigItem();
            case 1:
                return getLanguageItem();
            case 4:
                return getExitItem();
            default:
                break;
        }

        return getFillerItem();
    }

    @Override
    public @Nullable AbstractButton getButton(int slot) {
        switch (slot) {
            case 0:
                return getConfigButton();
            case 1:
                return getLanguageButton();
            case 4:
                return getExitButton();
            default:
                break;
        }

        return null;
    }

    @Override
    public @Nullable Component getTitle() {
        return null;
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return true;
    }

    private @NotNull ItemStack getFillerItem() {
        ItemBuilder builder = new ItemBuilder(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE);

        ItemHandler itemHandler = getItemHandler();
        if (itemHandler != null) {
            Component displayName = Component.empty();
            builder.withName(itemHandler, displayName);
        }

        return builder.build();
    }

    private @Nullable ItemStack getConfigItem() {
        // TODO
        return null;
    }

    private @Nullable ItemStack getLanguageItem() {
        // TODO
        return null;
    }

    private @Nullable ItemStack getExitItem() {
        return null;
    }

    private @Nullable AbstractButton getConfigButton() {
        // TODO
        return null;
    }

    private @NotNull AbstractButton getLanguageButton() {
        CorePlugin plugin = getCorePlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("language.yml");

        Player player = getPlayer();
        ConfigurationEditorMenu menu = new ConfigurationEditorMenu(this, plugin, player, configuration);
        return new OpenMenuButton(menu);
    }

    private @NotNull AbstractButton getExitButton() {
        return new ExitButton(this);
    }
}
