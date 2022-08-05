package com.github.sirblobman.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class PotionBuilder extends ItemBuilder {
    public PotionBuilder(BottleType bottleType) {
        super(bottleType.getItem());
    }

    public PotionBuilder withMainEffect(@NotNull PotionType potionType, boolean extended, boolean upgraded) {
        Validate.notNull(potionType, "potionType must not be null!");

        ItemMeta itemMeta = getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData potionData = new PotionData(potionType, extended, upgraded);
        potionMeta.setBasePotionData(potionData);

        return (PotionBuilder) withItemMeta(potionMeta);
    }

    public PotionBuilder withExtraEffect(@NotNull PotionEffect potionEffect) {
        Validate.notNull(potionEffect, "potionEffect must not be null!");

        ItemMeta itemMeta = getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.addCustomEffect(potionEffect, true);

        return (PotionBuilder) withItemMeta(potionMeta);
    }

    public PotionBuilder withColor(@NotNull Color color) {
        Validate.notNull(color, "color must not be null!");

        ItemMeta itemMeta = getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) {
            return this;
        }

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.setColor(color);

        return (PotionBuilder) withItemMeta(potionMeta);
    }

    public PotionBuilder withColor(int red, int green, int blue) {
        Color color = Color.fromRGB(red, green, blue);
        return withColor(color);
    }

    public PotionBuilder withColor(int rgb) {
        Color color = Color.fromRGB(rgb);
        return withColor(color);
    }
}
