package com.github.sirblobman.api.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.api.nms.ItemHandler;
import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.utility.VersionUtility;
import com.github.sirblobman.api.utility.paper.PaperChecker;
import com.github.sirblobman.api.utility.paper.PaperHelper;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public class ItemBuilder {
    protected ItemStack finalItem;

    public ItemBuilder(@NotNull ItemStack item) {
        this.finalItem = item.clone();
    }

    public ItemBuilder(@NotNull Material material) {
        this(new ItemStack(material, 1));
    }

    public ItemBuilder(@NotNull XMaterial material) {
        this(Validate.notNull(material.parseItem(), "material has an invalid item!"));
    }

    protected final @NotNull ItemStack getFinalItem() {
        return this.finalItem;
    }

    public @NotNull ItemStack build() {
        ItemStack finalItem = getFinalItem();
        return finalItem.clone();
    }

    public @NotNull ItemBuilder withMaterial(@NotNull Material material) {
        this.finalItem.setType(material);
        return this;
    }

    public @NotNull ItemBuilder withMaterial(@NotNull XMaterial material) {
        Material bukkitMaterial = material.parseMaterial();
        return (bukkitMaterial == null ? this : withMaterial(bukkitMaterial));
    }

    public @NotNull ItemBuilder withAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount cannot be less than 0!");
        }

        int maxStackSize = this.finalItem.getMaxStackSize();
        if(maxStackSize != -1 && amount > maxStackSize) {
            XMaterial material = XMaterial.matchXMaterial(this.finalItem);
            String messageFormat = "Tried to set amount to %s on material %s with max amount %s.";
            throw new IllegalArgumentException(String.format(messageFormat, amount, material, maxStackSize));
        }

        this.finalItem.setAmount(amount);
        return this;
    }

    public @NotNull ItemBuilder withMaxAmount() {
        int maxAmount = this.finalItem.getMaxStackSize();
        if (maxAmount == -1) {
            maxAmount = 64;
        }

        return withAmount(maxAmount);
    }

    public @Nullable ItemMeta getItemMeta() {
        return this.finalItem.getItemMeta();
    }

    public @NotNull ItemBuilder withItemMeta(@Nullable ItemMeta itemMeta) {
        this.finalItem.setItemMeta(itemMeta);
        return this;
    }

    public @NotNull ItemBuilder withDamage(int damage) {
        int minorVersion = VersionUtility.getMinorVersion();
        return (minorVersion < 13 ? withLegacyDamage((short) damage) : withModernDurability(damage));
    }

    @SuppressWarnings("deprecation")
    private @NotNull ItemBuilder withLegacyDamage(short damage) {
        this.finalItem.setDurability(damage);
        return this;
    }

    private @NotNull ItemBuilder withModernDurability(int damage) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta instanceof Damageable) {
            ((Damageable) itemMeta).setDamage(damage);
            return withItemMeta(itemMeta);
        }

        return this;
    }

    public @NotNull ItemBuilder withModel(@Nullable Integer model) {
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

    public @NotNull ItemBuilder withName(@Nullable String name) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.setDisplayName(name);
        return withItemMeta(itemMeta);
    }

    public @NotNull ItemBuilder withName(@NotNull ItemHandler itemHandler, @Nullable Component name) {
        if (name == null) {
            return withName(null);
        }

        if (PaperChecker.hasNativeComponentSupport()) {
            PaperHelper.setDisplayName(this.finalItem, name);
        } else {
            this.finalItem = itemHandler.setDisplayName(this.finalItem, name);
        }

        return this;
    }

    public @NotNull ItemBuilder withLore(@Nullable List<String> loreList) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.setLore(loreList);
        return withItemMeta(itemMeta);
    }

    public @NotNull ItemBuilder withLore(String @NotNull ... loreArray) {
        List<String> loreList = new ArrayList<>();
        Collections.addAll(loreList, loreArray);
        return withLore(loreList);
    }

    public @NotNull ItemBuilder withLore(@NotNull ItemHandler itemHandler, @Nullable List<Component> lore) {
        if (lore == null) {
            return withLore((List<String>) null);
        }

        if (PaperChecker.hasNativeComponentSupport()) {
            PaperHelper.setLore(this.finalItem, lore);
        } else {
            this.finalItem = itemHandler.setLore(this.finalItem, lore);
        }

        return this;
    }

    public @NotNull ItemBuilder withLore(@NotNull ItemHandler itemHandler, Component @NotNull ... lines) {
        List<Component> lore = new ArrayList<>();
        Collections.addAll(lore, lines);
        return withLore(itemHandler, lore);
    }

    public @NotNull ItemBuilder appendLore(@NotNull String line) {
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

    public @NotNull ItemBuilder appendLore(@NotNull ItemHandler itemHandler, @NotNull Component line) {
        boolean paper = PaperChecker.hasNativeComponentSupport();
        return (paper ? appendLorePaper(line) : appendLoreSpigot(itemHandler, line));
    }

    private @NotNull ItemBuilder appendLorePaper(@NotNull Component line) {
        List<Component> lore = PaperHelper.getLore(this.finalItem);
        lore = (lore == null ? new ArrayList<>() : new ArrayList<>(lore));
        lore.add(line);

        PaperHelper.setLore(this.finalItem, lore);
        return this;
    }

    private @NotNull ItemBuilder appendLoreSpigot(@NotNull ItemHandler itemHandler, @NotNull Component line) {
        List<Component> lore = itemHandler.getLore(this.finalItem);
        lore = (lore == null ? new ArrayList<>() : new ArrayList<>(lore));
        lore.add(line);

        this.finalItem = itemHandler.setLore(this.finalItem, lore);
        return this;
    }

    public @NotNull ItemBuilder withEnchantment(@NotNull Enchantment enchantment, int level) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.addEnchant(enchantment, level, true);
        return withItemMeta(itemMeta);
    }

    public @NotNull ItemBuilder withFlags(ItemFlag @NotNull ... flagArray) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return this;
        }

        itemMeta.addItemFlags(flagArray);
        return withItemMeta(itemMeta);
    }

    public @NotNull ItemBuilder withGlowing() {
        return withEnchantment(Enchantment.LUCK, 1).withFlags(ItemFlag.HIDE_ENCHANTS);
    }
}
