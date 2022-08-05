package com.github.sirblobman.api.item;

import org.bukkit.inventory.EquipmentSlot;

import com.github.sirblobman.api.utility.Validate;

import com.cryptomorin.xseries.XMaterial;
import org.jetbrains.annotations.NotNull;

public enum ArmorType {
    HELMET(EquipmentSlot.HEAD),
    CHESTPLATE(EquipmentSlot.CHEST),
    LEGGINGS(EquipmentSlot.LEGS),
    BOOTS(EquipmentSlot.FEET)
    ;

    private final EquipmentSlot equipmentSlot;

    ArmorType(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = Validate.notNull(equipmentSlot, "equipmentSlot must not be null!");
    }

    @NotNull
    public EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    @NotNull
    public XMaterial getArmorMaterial(ArmorMaterialType materialType) {
        Validate.notNull(materialType, "materialType must not be null!");
        return materialType.getArmorMaterial(this);
    }
}
