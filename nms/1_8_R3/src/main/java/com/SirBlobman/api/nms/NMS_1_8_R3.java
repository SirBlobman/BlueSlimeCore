package com.SirBlobman.api.nms;

public class NMS_1_8_R3 extends NMS_1_8_R1 {
    private final PlayerHandler playerHandler = new PlayerHandler_1_8_R3();
    
    @Override
    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
}