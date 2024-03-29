package com.github.sirblobman.api.item;

import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

import static com.github.sirblobman.api.shaded.xseries.XMaterial.CHAINMAIL_BOOTS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.CHAINMAIL_CHESTPLATE;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.CHAINMAIL_HELMET;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.CHAINMAIL_LEGGINGS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.DIAMOND_BOOTS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.DIAMOND_CHESTPLATE;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.DIAMOND_HELMET;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.DIAMOND_LEGGINGS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.GOLDEN_BOOTS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.GOLDEN_CHESTPLATE;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.GOLDEN_HELMET;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.GOLDEN_LEGGINGS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.IRON_BOOTS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.IRON_CHESTPLATE;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.IRON_HELMET;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.IRON_LEGGINGS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.LEATHER_BOOTS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.LEATHER_CHESTPLATE;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.LEATHER_HELMET;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.LEATHER_LEGGINGS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.NETHERITE_BOOTS;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.NETHERITE_CHESTPLATE;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.NETHERITE_HELMET;
import static com.github.sirblobman.api.shaded.xseries.XMaterial.NETHERITE_LEGGINGS;

public enum ArmorMaterialType {
    LEATHER(LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS),
    IRON(IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS),
    GOLDEN(GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS),
    CHAINMAIL(CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS),
    DIAMOND(DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS),
    NETHERITE(NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS);

    private static final Map<XMaterial, ArmorMaterialType> BY_MATERIAL;

    static {
        BY_MATERIAL = new EnumMap<>(XMaterial.class);
        ArmorMaterialType[] values = values();
        for (ArmorMaterialType type : values) {
            XMaterial helmet = type.getHelmet();
            XMaterial chestplate = type.getChestplate();
            XMaterial leggings = type.getLeggings();
            XMaterial boots = type.getBoots();
            BY_MATERIAL.put(helmet, type);
            BY_MATERIAL.put(chestplate, type);
            BY_MATERIAL.put(leggings, type);
            BY_MATERIAL.put(boots, type);
        }
    }

    public static @Nullable ArmorMaterialType getByMaterial(@NotNull XMaterial material) {
        return BY_MATERIAL.get(material);
    }

    private final XMaterial helmet;
    private final XMaterial chestplate;
    private final XMaterial leggings;
    private final XMaterial boots;

    ArmorMaterialType(@NotNull XMaterial helmet, @NotNull XMaterial chestplate, @NotNull XMaterial leggings,
                      @NotNull XMaterial boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public @NotNull XMaterial getHelmet() {
        return this.helmet;
    }

    public @NotNull XMaterial getChestplate() {
        return this.chestplate;
    }

    public @NotNull XMaterial getLeggings() {
        return this.leggings;
    }

    public @NotNull XMaterial getBoots() {
        return this.boots;
    }

    public @NotNull XMaterial getArmorMaterial(@NotNull ArmorType armorType) {
        switch (armorType) {
            case HELMET:
                return getHelmet();
            case CHESTPLATE:
                return getChestplate();
            case LEGGINGS:
                return getLeggings();
            case BOOTS:
                return getBoots();
            default:
                break;
        }

        throw new IllegalStateException("Unknown armorType '" + armorType + "'.");
    }
}
