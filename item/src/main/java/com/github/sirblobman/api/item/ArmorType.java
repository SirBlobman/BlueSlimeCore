package com.github.sirblobman.api.item;

import java.util.EnumMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.inventory.EquipmentSlot;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

public enum ArmorType {
    HELMET(EquipmentSlot.HEAD),
    CHESTPLATE(EquipmentSlot.CHEST),
    LEGGINGS(EquipmentSlot.LEGS),
    BOOTS(EquipmentSlot.FEET);

    private static final Map<EquipmentSlot, ArmorType> BY_SLOT;

    static {
        BY_SLOT = new EnumMap<>(EquipmentSlot.class);
        ArmorType[] values = values();
        for (ArmorType armorType : values) {
            EquipmentSlot slot = armorType.getEquipmentSlot();
            BY_SLOT.put(slot, armorType);
        }
    }

    public static @Nullable ArmorType getBySlot(@NotNull EquipmentSlot slot) {
        return BY_SLOT.get(slot);
    }

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
