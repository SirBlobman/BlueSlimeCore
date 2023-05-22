package com.github.sirblobman.api.item;

import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.inventory.ItemStack;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

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

    private static final Map<XMaterial, BottleType> BY_MATERIAL;

    static {
        BY_MATERIAL = new EnumMap<>(XMaterial.class);
        BottleType[] values = values();
        for (BottleType bottleType : values) {
            XMaterial material = bottleType.getMaterial();
            BY_MATERIAL.put(material, bottleType);
        }
    }

    public static @Nullable BottleType getByMaterial(@NotNull XMaterial material) {
        return BY_MATERIAL.get(material);
    }

    private final XMaterial material;

    BottleType(@NotNull XMaterial material) {
        this.material = material;
    }

    /**
     * @return A {@link XMaterial} value that matches this potion type.
     */
    public @NotNull XMaterial getMaterial() {
        return this.material;
    }

    /**
     * @return An {@link ItemStack} that matches this potion type, or {@code null}.
     */
    public @Nullable ItemStack getItem() {
        XMaterial material = getMaterial();
        return material.parseItem();
    }
}
