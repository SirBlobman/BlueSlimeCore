package com.github.sirblobman.api.item;

import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum BottleType {
    /**
     * A regular potion bottle that players can drink.
     */
    BOTTLE,
    
    /**
     * A potion that can be thrown on the ground.
     */
    SPLASH,
    
    /**
     * A potion that can be thrown on the ground and stays there for a bit.
     */
    LINGERING;
    
    /**
     * @return A {@link XMaterial} value that matches this potion type.
     */
    @NotNull
    public XMaterial getXMaterial() {
        switch(this) {
            case BOTTLE:
                return XMaterial.POTION;
            case SPLASH:
                return XMaterial.SPLASH_POTION;
            case LINGERING:
                return XMaterial.LINGERING_POTION;
            default:
                break;
        }
        
        return XMaterial.POTION;
    }
    
    /**
     * @return An {@link ItemStack} that matches this potion type, or {@code null}.
     */
    @Nullable
    public ItemStack getItem() {
        XMaterial material = getXMaterial();
        return material.parseItem();
    }
}
