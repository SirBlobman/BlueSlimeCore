package com.github.sirblobman.api.core.menu;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.menu.AbstractPagedMenu;
import com.github.sirblobman.api.menu.IMenu;
import com.github.sirblobman.api.menu.button.AbstractButton;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.Nullable;

// TODO
public final class ConfigurationEditorMenu extends AbstractPagedMenu {
    private final YamlConfiguration configuration;

    public ConfigurationEditorMenu(IMenu parent, JavaPlugin plugin, Player player, YamlConfiguration configuration) {
        super(parent, plugin, player);
        this.configuration = Validate.notNull(configuration, "configuration must not be null!");
    }

    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return null;
    }

    @Override
    public @Nullable AbstractButton getButton(int slot) {
        return null;
    }

    @Override
    public boolean shouldPreventClick(int slot) {
        return false;
    }

    @Override
    public int getMaxPages() {
        return 0;
    }

    @Override
    public Component getTitleFormat() {
        return null;
    }

    @Override
    public @Nullable MultiVersionHandler getMultiVersionHandler() {
        return super.getMultiVersionHandler();
    }

    @Override
    public @Nullable LanguageManager getLanguageManager() {
        return super.getLanguageManager();
    }
}
