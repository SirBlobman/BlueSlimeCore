package com.github.sirblobman.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;

import com.cryptomorin.xseries.XMaterial;
import org.jetbrains.annotations.Nullable;

public class ItemBuilder {
    protected final ItemStack finalItem;

    public ItemBuilder(ItemStack item) {
        this.finalItem = Validate.notNull(item, "item must not be null!");
    }

    public ItemBuilder(Material material) {
        Validate.notNull(material, "material must not be null!");
        this.finalItem = new ItemStack(material, 1);
    }

    public ItemBuilder(XMaterial material) {
        Validate.notNull(material, "material must not be null!");
        this.finalItem = material.parseItem();
    }

    final ItemStack getFinalItem() {
        return this.finalItem;
    }

    public ItemStack build() {
        return this.finalItem.clone();
    }

    public ItemBuilder withMaterial(Material material) {
        this.finalItem.setType(material);
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be less than 0!");
        }

        if (amount > 64) {
            throw new IllegalArgumentException("amount cannot be greater than 64!");
        }

        this.finalItem.setAmount(amount);
        return this;
    }

    public ItemBuilder withMaxAmount() {
        int maxAmount = this.finalItem.getMaxStackSize();
        return withAmount(maxAmount);
    }

    @Nullable
    public ItemMeta getItemMeta() {
        return this.finalItem.getItemMeta();
    }

    public ItemBuilder withItemMeta(@Nullable ItemMeta itemMeta) {
        this.finalItem.setItemMeta(itemMeta);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder withDamage(int damage) {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 13) {
            short shortDamage = (short) damage;
            this.finalItem.setDurability(shortDamage);
            return this;
        }

        ItemMeta itemMeta = this.finalItem.getItemMeta();
        if (itemMeta instanceof Damageable) {
            ((Damageable) itemMeta).setDamage(damage);
            return withItemMeta(itemMeta);
        }

        return this;
    }

    public ItemBuilder withModel(@Nullable Integer model) {
        int minorVersion = VersionUtility.getMinorVersion();
        if (minorVersion < 14) {
            return this;
        }

        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.setCustomModelData(model);
        return withItemMeta(itemMeta);
    }

    public ItemBuilder withName(String name) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.setDisplayName(name);
        return withItemMeta(itemMeta);
    }

    public ItemBuilder withLore(List<String> loreList) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.setLore(loreList);
        return withItemMeta(itemMeta);
    }

    public ItemBuilder withLore(String... loreArray) {
        List<String> loreList = new ArrayList<>();
        Collections.addAll(loreList, loreArray);
        return withLore(loreList);
    }

    public ItemBuilder appendLore(String line) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null || !itemMeta.hasLore()) {
            return withLore(line);
        }

        List<String> oldLore = itemMeta.getLore();
        List<String> newLore = (oldLore == null ? new ArrayList<>() : new ArrayList<>(oldLore));
        newLore.add(line);

        itemMeta.setLore(newLore);
        return withItemMeta(itemMeta);
    }

    public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.addEnchant(enchantment, level, true);
        return withItemMeta(itemMeta);
    }

    public ItemBuilder withFlags(ItemFlag... flagArray) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.addItemFlags(flagArray);
        return withItemMeta(itemMeta);
    }

    public ItemBuilder withGlowing() {
        return withEnchantment(Enchantment.LUCK, 1).withFlags(ItemFlag.HIDE_ENCHANTS);
    }
}
