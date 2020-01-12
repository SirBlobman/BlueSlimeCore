package com.SirBlobman.api.nms;

import org.bukkit.entity.LivingEntity;

public class EntityHandler_1_8_R1 extends EntityHandler_1_7_R4 {
    @Override
    public String getName(LivingEntity entity) {
        return entity.getName();
    }
}