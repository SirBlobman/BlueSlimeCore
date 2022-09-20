package com.github.sirblobman.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.language.ComponentHelper;
import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.utility.Validate;

public final class ComponentItemBuilder {
    private final ItemHandler itemHandler;
    private ItemStack item;

    public ComponentItemBuilder(ItemHandler itemHandler, ItemStack baseItem) {
        this.itemHandler = Validate.notNull(itemHandler, "itemHandler must not be null!");
        this.item = Validate.notNull(baseItem, "baseItem must not be null!").clone();
    }

    private ItemHandler getItemHandler() {
        return this.itemHandler;
    }

    private ItemStack getItem() {
        return this.item;
    }

    public ItemBuilder asItemBuilder() {
        ItemStack item = getItem();
        return new ItemBuilder(item);
    }

    public ComponentItemBuilder withName(Component displayName) {
        ItemStack item = getItem();
        ItemHandler itemHandler = getItemHandler();

        Component realDisplayName = ComponentHelper.wrapNoItalics(displayName);
        this.item = itemHandler.setDisplayName(item, realDisplayName);
        return this;
    }

    public ComponentItemBuilder withLore(List<Component> lore) {
        ItemStack item = getItem();
        ItemHandler itemHandler = getItemHandler();

        List<Component> realLore = ComponentHelper.wrapNoItalics(lore);
        this.item = itemHandler.setLore(item, realLore);
        return this;
    }

    public ComponentItemBuilder withLore(Component... lines) {
        List<Component> lore = new ArrayList<>();
        Collections.addAll(lore, lines);
        return withLore(lore);
    }
}
