package com.github.sirblobman.api.core.menu;

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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ConfigurationSelectorMenu extends AbstractMenu {
    private final CorePlugin plugin;

    public ConfigurationSelectorMenu(CorePlugin plugin, Player player) {
        super(plugin, player);
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public MultiVersionHandler getMultiVersionHandler() {
        CorePlugin plugin = getCorePlugin();
        return plugin.getMultiVersionHandler();
    }

    @NotNull
    @Override
    public LanguageManager getLanguageManager() {
        CorePlugin plugin = getCorePlugin();
        return plugin.getLanguageManager();
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Nullable
    @Override
    public ItemStack getItem(int slot) {
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

    @Nullable
    @Override
    public AbstractButton getButton(int slot) {
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

    @Nullable
    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return true;
    }

    private CorePlugin getCorePlugin() {
        return this.plugin;
    }

    private ItemStack getFillerItem() {
        ItemBuilder builder = new ItemBuilder(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE);

        ItemHandler itemHandler = getItemHandler();
        if (itemHandler != null) {
            Component displayName = Component.empty();
            builder.withName(itemHandler, displayName);
        }

        return builder.build();
    }

    private ItemStack getConfigItem() {
        // TODO
        return null;
    }

    private ItemStack getLanguageItem() {
        // TODO
        return null;
    }

    private ItemStack getExitItem() {
        return null;
    }

    private AbstractButton getConfigButton() {
        // TODO
        return null;
    }

    private AbstractButton getLanguageButton() {
        CorePlugin plugin = getCorePlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        YamlConfiguration configuration = configurationManager.get("language.yml");

        Player player = getPlayer();
        ConfigurationEditorMenu menu = new ConfigurationEditorMenu(this, plugin, player, configuration);
        return new OpenMenuButton(menu);
    }

    private AbstractButton getExitButton() {
        return new ExitButton(this);
    }
}
