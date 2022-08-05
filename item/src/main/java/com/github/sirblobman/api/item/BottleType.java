package com.github.sirblobman.api.item;

import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.utility.Validate;

import com.cryptomorin.xseries.XMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum BottleType {
    /**
     * A regular potion bottle that players can drink.
     */
    BOTTLE(XMaterial.POTION),

    /**
     * A potion that can be thrown on the ground.
     */
    SPLASH(XMaterial.SPLASH_POTION),

    /**
     * A potion that can be thrown on the ground and stays there for a bit.
     */
    LINGERING(XMaterial.LINGERING_POTION);

    private final XMaterial material;

    BottleType(XMaterial material) {
        this.material = Validate.notNull(material, "material must not be null!");
    }

    /**
     * @return A {@link XMaterial} value that matches this potion type.
     */
    @NotNull
    public XMaterial getMaterial() {
        return this.material;
    }

    /**
     * @return An {@link ItemStack} that matches this potion type, or {@code null}.
     */
    @Nullable
    public ItemStack getItem() {
        XMaterial material = getMaterial();
        return material.parseItem();
    }
}
