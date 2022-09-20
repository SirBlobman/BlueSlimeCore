package com.github.sirblobman.api.item;

import com.github.sirblobman.api.utility.Validate;
import com.github.sirblobman.api.xseries.XMaterial;

import org.jetbrains.annotations.NotNull;

import static com.github.sirblobman.api.xseries.XMaterial.CHAINMAIL_BOOTS;
import static com.github.sirblobman.api.xseries.XMaterial.CHAINMAIL_CHESTPLATE;
import static com.github.sirblobman.api.xseries.XMaterial.CHAINMAIL_HELMET;
import static com.github.sirblobman.api.xseries.XMaterial.CHAINMAIL_LEGGINGS;
import static com.github.sirblobman.api.xseries.XMaterial.DIAMOND_BOOTS;
import static com.github.sirblobman.api.xseries.XMaterial.DIAMOND_CHESTPLATE;
import static com.github.sirblobman.api.xseries.XMaterial.DIAMOND_HELMET;
import static com.github.sirblobman.api.xseries.XMaterial.DIAMOND_LEGGINGS;
import static com.github.sirblobman.api.xseries.XMaterial.GOLDEN_BOOTS;
import static com.github.sirblobman.api.xseries.XMaterial.GOLDEN_CHESTPLATE;
import static com.github.sirblobman.api.xseries.XMaterial.GOLDEN_HELMET;
import static com.github.sirblobman.api.xseries.XMaterial.GOLDEN_LEGGINGS;
import static com.github.sirblobman.api.xseries.XMaterial.IRON_BOOTS;
import static com.github.sirblobman.api.xseries.XMaterial.IRON_CHESTPLATE;
import static com.github.sirblobman.api.xseries.XMaterial.IRON_HELMET;
import static com.github.sirblobman.api.xseries.XMaterial.IRON_LEGGINGS;
import static com.github.sirblobman.api.xseries.XMaterial.LEATHER_BOOTS;
import static com.github.sirblobman.api.xseries.XMaterial.LEATHER_CHESTPLATE;
import static com.github.sirblobman.api.xseries.XMaterial.LEATHER_HELMET;
import static com.github.sirblobman.api.xseries.XMaterial.LEATHER_LEGGINGS;
import static com.github.sirblobman.api.xseries.XMaterial.NETHERITE_BOOTS;
import static com.github.sirblobman.api.xseries.XMaterial.NETHERITE_CHESTPLATE;
import static com.github.sirblobman.api.xseries.XMaterial.NETHERITE_HELMET;
import static com.github.sirblobman.api.xseries.XMaterial.NETHERITE_LEGGINGS;

public enum ArmorMaterialType {
    LEATHER(LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS),
    IRON(IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS),
    GOLDEN(GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS),
    CHAINMAIL(CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS),
    DIAMOND(DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS),
    NETHERITE(NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS);

    private final XMaterial helmet;
    private final XMaterial chestplate;
    private final XMaterial leggings;
    private final XMaterial boots;

    ArmorMaterialType(XMaterial helmet, XMaterial chestplate, XMaterial leggings, XMaterial boots) {
        this.helmet = Validate.notNull(helmet, "helmet XMaterial must not be null!");
        this.chestplate = Validate.notNull(chestplate, "chestplate XMaterial must not be null!");
        this.leggings = Validate.notNull(leggings, "leggings XMaterial must not be null!");
        this.boots = Validate.notNull(boots, "boots XMaterial must not be null!");
    }

    @NotNull
    public XMaterial getHelmet() {
        return this.helmet;
    }

    @NotNull
    public XMaterial getChestplate() {
        return this.chestplate;
    }

    @NotNull
    public XMaterial getLeggings() {
        return this.leggings;
    }

    @NotNull
    public XMaterial getBoots() {
        return this.boots;
    }

    @NotNull
    public XMaterial getArmorMaterial(ArmorType armorType) {
        Validate.notNull(armorType, "armorType must not be null!");

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
