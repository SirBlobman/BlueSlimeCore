package com.SirBlobman.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.SirBlobman.api.utility.Validate;
import com.SirBlobman.api.utility.VersionUtility;

import com.cryptomorin.xseries.XMaterial;

public class ItemBuilder {
    protected final ItemStack finalItem;
    public ItemBuilder(ItemStack item) {
        this.finalItem = Validate.notNull(item, "item must not be null!");
    }

    public ItemStack build() {
        return this.finalItem.clone();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material, 1));
    }

    public ItemBuilder(XMaterial xMaterial) {
        this(xMaterial.parseItem());
    }

    public ItemBuilder withMaterial(Material type) {
        this.finalItem.setType(type);
        return this;
    }

    public ItemBuilder withAmount(int amount) {
        if(amount < 0) throw new IllegalArgumentException("amount cannot be less than 0!");
        if(amount > 64) throw new IllegalArgumentException("amount cannot be greater than 64!");
        this.finalItem.setAmount(amount);
        return this;
    }

    public ItemBuilder withMaxAmount() {
        int maxAmount = this.finalItem.getMaxStackSize();
        return withAmount(maxAmount);
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder withDamage(int damage) {
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 13) {
            short shortDamage = (short) damage;
            this.finalItem.setDurability(shortDamage);
            return this;
        }

        ItemMeta meta = this.finalItem.getItemMeta();
        if(meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            damageable.setDamage(damage);
            this.finalItem.setItemMeta(meta);
        }

        return this;
    }

    public ItemBuilder withName(String name) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(meta == null) return this;

        meta.setDisplayName(name);
        this.finalItem.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withLore(List<String> loreList) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(meta == null) return this;

        meta.setLore(loreList);
        this.finalItem.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withLore(String... loreArray) {
        List<String> loreList = new ArrayList<>();
        Collections.addAll(loreList, loreArray);
        return withLore(loreList);
    }

    public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(meta == null) return this;

        meta.addEnchant(enchantment, level, true);
        this.finalItem.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withFlags(ItemFlag... flagArray) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(meta == null) return this;

        meta.addItemFlags(flagArray);
        this.finalItem.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withGlowing() {
        return withEnchantment(Enchantment.LUCK, 1).withFlags(ItemFlag.HIDE_ENCHANTS);
    }
}