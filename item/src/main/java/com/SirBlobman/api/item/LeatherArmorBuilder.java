package com.SirBlobman.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ItemBuilder {
    public LeatherArmorBuilder(ArmorType armorType) {
        super(armorType.getArmorMaterial("LEATHER"));
    }

    public LeatherArmorBuilder withColor(Color color) {
        ItemMeta meta = this.finalItem.getItemMeta();
        if(!(meta instanceof LeatherArmorMeta)) return this;
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;

        leatherArmorMeta.setColor(color);
        this.finalItem.setItemMeta(leatherArmorMeta);
        return this;
    }

    public LeatherArmorBuilder withColor(int red, int green, int blue) {
        Color color = Color.fromRGB(red, green, blue);
        return withColor(color);
    }

    public LeatherArmorBuilder withColor(int rgb) {
        Color color = Color.fromRGB(rgb);
        return withColor(color);
    }
}