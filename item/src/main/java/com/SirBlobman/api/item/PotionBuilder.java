package com.SirBlobman.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public class PotionBuilder extends ItemBuilder {
    public PotionBuilder(BottleType bottleType) {
        super(bottleType.getItem());
    }

    public PotionBuilder withMainEffect(PotionType potionType, boolean extended, boolean upgraded) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(!(meta instanceof PotionMeta)) return this;
        PotionMeta potionMeta = (PotionMeta) meta;

        PotionData potionData = new PotionData(potionType, extended, upgraded);
        potionMeta.setBasePotionData(potionData);

        this.finalItem.setItemMeta(potionMeta);
        return this;
    }

    public PotionBuilder withExtraEffect(PotionEffect potionEffect) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(!(meta instanceof PotionMeta)) return this;
        PotionMeta potionMeta = (PotionMeta) meta;

        potionMeta.addCustomEffect(potionEffect, true);

        this.finalItem.setItemMeta(potionMeta);
        return this;
    }

    public PotionBuilder withColor(Color color) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(!(meta instanceof PotionMeta)) return this;
        PotionMeta potionMeta = (PotionMeta) meta;

        potionMeta.setColor(color);

        this.finalItem.setItemMeta(potionMeta);
        return this;
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