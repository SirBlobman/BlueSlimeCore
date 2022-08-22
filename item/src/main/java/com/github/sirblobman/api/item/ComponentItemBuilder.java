package com.github.sirblobman.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.utility.Validate;

import net.kyori.adventure.text.Component;

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
        this.item = itemHandler.setDisplayName(item, displayName);
        return this;
    }

    public ComponentItemBuilder withLore(List<Component> lore) {
        ItemStack item = getItem();
        ItemHandler itemHandler = getItemHandler();
        this.item = itemHandler.setLore(item, lore);
        return this;
    }

    public ComponentItemBuilder withLore(Component... lines) {
        List<Component> lore = new ArrayList<>();
        Collections.addAll(lore, lines);
        return withLore(lore);
    }
}
