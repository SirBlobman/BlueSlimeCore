package com.github.sirblobman.api.item;

import org.jetbrains.annotations.NotNull;

import org.bukkit.inventory.EquipmentSlot;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

public enum ArmorType {
    HELMET(EquipmentSlot.HEAD),
    CHESTPLATE(EquipmentSlot.CHEST),
    LEGGINGS(EquipmentSlot.LEGS),
    BOOTS(EquipmentSlot.FEET);

    private final EquipmentSlot equipmentSlot;

    ArmorType(@NotNull EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }

    public @NotNull XMaterial getArmorMaterial(@NotNull ArmorMaterialType materialType) {
        return materialType.getArmorMaterial(this);
    }
}
