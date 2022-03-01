package com.github.sirblobman.api.item;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

import com.cryptomorin.xseries.XMaterial;
import org.jetbrains.annotations.Nullable;

public enum ArmorType {
    HELMET, CHESTPLATE, LEGGINGS, BOOTS;
    
    @Nullable
    public EquipmentSlot getSlot() {
        switch(this) {
            case HELMET:
                return EquipmentSlot.HEAD;
            case CHESTPLATE:
                return EquipmentSlot.CHEST;
            case LEGGINGS:
                return EquipmentSlot.LEGS;
            case BOOTS:
                return EquipmentSlot.FEET;
            default:
                break;
        }
        
        return null;
    }
    
    @Deprecated
    public Material getArmorMaterial(String type) {
        String materialName = (type + "_" + name());
        return Material.matchMaterial(materialName);
    }
    
    public Optional<XMaterial> getArmorMaterial(ArmorMaterialType armorMaterialType) {
        String typeName = armorMaterialType.name();
        String armorName = this.name();
        String materialName = (typeName + "_" + armorName);
        return XMaterial.matchXMaterial(materialName);
    }
}
