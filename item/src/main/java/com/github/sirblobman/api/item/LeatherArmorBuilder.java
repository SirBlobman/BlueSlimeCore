package com.github.sirblobman.api.item;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public final class LeatherArmorBuilder extends ItemBuilder {
    public LeatherArmorBuilder(@NotNull ArmorType armorType) {
        super(armorType.getArmorMaterial(ArmorMaterialType.LEATHER));
    }

    public @NotNull LeatherArmorBuilder withColor(@NotNull Color color) {
        ItemStack finalItem = getFinalItem();
        ItemMeta itemMeta = finalItem.getItemMeta();
        if (!(itemMeta instanceof LeatherArmorMeta)) {
            return this;
        }

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        leatherArmorMeta.setColor(color);

        return (LeatherArmorBuilder) withItemMeta(itemMeta);
    }

    public @NotNull LeatherArmorBuilder withColor(int red, int green, int blue) {
        Color color = Color.fromRGB(red, green, blue);
        return withColor(color);
    }

    public @NotNull LeatherArmorBuilder withColor(int rgb) {
        Color color = Color.fromRGB(rgb);
        return withColor(color);
    }
}
