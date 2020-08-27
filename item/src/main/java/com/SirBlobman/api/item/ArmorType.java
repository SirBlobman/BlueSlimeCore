package com.SirBlobman.api.item;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

public enum ArmorType {
    HELMET, CHESTPLATE, LEGGINGS, BOOTS;

    public EquipmentSlot getSlot() {
        switch(this) {
            case HELMET: return EquipmentSlot.HEAD;
            case CHESTPLATE: return EquipmentSlot.CHEST;
            case LEGGINGS: return EquipmentSlot.LEGS;
            case BOOTS: return EquipmentSlot.FEET;
            default: break;
        }

        return null;
    }

    public Material getArmorMaterial(String type) {
        String materialName = (type + "_" + name());
        return Material.matchMaterial(materialName);
    }
}
