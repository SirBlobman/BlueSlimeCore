package com.github.sirblobman.api.item;

import java.util.Optional;

import com.cryptomorin.xseries.XMaterial;

public enum ArmorMaterialType {
    LEATHER, IRON, GOLDEN, CHAINMAIL, DIAMOND, NETHERITE;

    public Optional<XMaterial> getArmorMaterial(ArmorType armorType) {
        String typeName = this.name();
        String armorName = armorType.name();
        String materialName = (typeName + "_" + armorName);
        return XMaterial.matchXMaterial(materialName);
    }
}
