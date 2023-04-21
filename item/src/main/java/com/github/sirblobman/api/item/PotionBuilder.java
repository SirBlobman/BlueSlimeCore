package com.github.sirblobman.api.item;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public final class PotionBuilder extends ItemBuilder {
    public PotionBuilder(@NotNull BottleType bottleType) {
        super(bottleType.getItem());
    }

    public @NotNull PotionBuilder withMainEffect(@NotNull PotionType potionType, boolean extended, boolean upgraded) {
        ItemMeta itemMeta = getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData potionData = new PotionData(potionType, extended, upgraded);
        potionMeta.setBasePotionData(potionData);

        return (PotionBuilder) withItemMeta(potionMeta);
    }

    public @NotNull PotionBuilder withExtraEffect(@NotNull PotionEffect potionEffect) {
        ItemMeta itemMeta = getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.addCustomEffect(potionEffect, true);

        return (PotionBuilder) withItemMeta(potionMeta);
    }

    public @NotNull PotionBuilder withColor(@NotNull Color color) {
        ItemMeta itemMeta = getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.setColor(color);

        return (PotionBuilder) withItemMeta(potionMeta);
    }

    public @NotNull PotionBuilder withColor(int red, int green, int blue) {
        Color color = Color.fromRGB(red, green, blue);
        return withColor(color);
    }

    public @NotNull PotionBuilder withColor(int rgb) {
        Color color = Color.fromRGB(rgb);
        return withColor(color);
    }
}
