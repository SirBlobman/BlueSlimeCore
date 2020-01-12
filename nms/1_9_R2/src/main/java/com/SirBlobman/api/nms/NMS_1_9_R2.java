package com.SirBlobman.api.nms;

public class NMS_1_9_R2 extends NMS_1_9_R1 {
    private final PlayerHandler playerHandler = new PlayerHandler_1_9_R2();
    
    @Override
    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
}