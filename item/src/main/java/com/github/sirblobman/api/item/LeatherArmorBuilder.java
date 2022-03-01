package com.github.sirblobman.api.item;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.github.sirblobman.api.utility.Validate;

import com.cryptomorin.xseries.XMaterial;
import org.jetbrains.annotations.NotNull;

public final class LeatherArmorBuilder extends ItemBuilder {
    public LeatherArmorBuilder(ArmorType armorType) {
        super(armorType.getArmorMaterial(ArmorMaterialType.LEATHER).orElse(XMaterial.LEATHER_CHESTPLATE));
    }
    
    public LeatherArmorBuilder withColor(@NotNull Color color) {
        Validate.notNull(color, "color must not be null!");
        
        ItemStack finalItem = getFinalItem();
        ItemMeta itemMeta = finalItem.getItemMeta();
        if(!(itemMeta instanceof LeatherArmorMeta)) return this;
        
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        leatherArmorMeta.setColor(color);
        
        return (LeatherArmorBuilder) withItemMeta(itemMeta);
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
