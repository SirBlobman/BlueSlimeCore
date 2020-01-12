package com.SirBlobman.api.nms;

public class NMS_1_12_R1 extends NMS_1_9_R1 {
    private final PlayerHandler playerHandler = new PlayerHandler_1_12_R1();
    private final EntityHandler entityHandler = new EntityHandler_1_12_R1();
    
    @Override
    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
    
    @Override
    public EntityHandler getEntityHandler() {
        return this.entityHandler;
    }
}