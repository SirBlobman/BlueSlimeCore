package com.SirBlobman.api.nms;

public class NMS_1_14_R1 extends NMS_1_12_R1 {
    private final PlayerHandler playerHandler = new PlayerHandler_1_14_R1();
    
    @Override
    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
}