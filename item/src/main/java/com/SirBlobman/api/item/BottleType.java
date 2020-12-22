package com.SirBlobman.api.item;

import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

public enum BottleType {
    BOTTLE, SPLASH, LINGERING;

    public ItemStack getItem() {
        switch(this) {
            case BOTTLE: return XMaterial.POTION.parseItem();
            case SPLASH: return XMaterial.SPLASH_POTION.parseItem();
            case LINGERING: return XMaterial.LINGERING_POTION.parseItem();
            default: break;
        }

        return null;
    }
}
